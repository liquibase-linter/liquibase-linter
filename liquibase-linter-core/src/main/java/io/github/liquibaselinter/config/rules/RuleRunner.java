package io.github.liquibaselinter.config.rules;

import com.google.common.base.Strings;
import com.google.common.collect.Streams;
import io.github.liquibaselinter.ChangeLogLintingException;
import io.github.liquibaselinter.config.Config;
import io.github.liquibaselinter.report.Report;
import io.github.liquibaselinter.report.ReportItem;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@SuppressWarnings("unchecked")
public class RuleRunner {

    private static final String LQL_IGNORE_TOKEN = "lql-ignore";

    private final Config config;
    private final List<ChangeRule> changeRules = Streams.stream(ServiceLoader.load(ChangeRule.class)).collect(toList());
    private final List<ChangeSetRule> changeSetRules = Streams.stream(ServiceLoader.load(ChangeSetRule.class)).collect(toList());
    private final List<ChangeLogRule> changeLogRules = Streams.stream(ServiceLoader.load(ChangeLogRule.class)).collect(toList());
    private final List<ReportItem> reportItems;
    private final Set<String> filesParsed;

    public RuleRunner(Config config, List<ReportItem> reportItems, Set<String> filesParsed) {
        this.config = config;
        this.reportItems = reportItems;
        this.filesParsed = filesParsed;
    }

    public Report buildReport() {
        return new Report(config, reportItems);
    }

    public Set<String> getFilesParsed() {
        return filesParsed;
    }

    public void checkChange(Change change) throws ChangeLogLintingException {
        final ChangeSet changeSet = change.getChangeSet();
        final DatabaseChangeLog changeLog = changeSet.getChangeLog();

        for (ChangeRule changeRule : changeRules) {
            if (changeRule.getChangeType().isAssignableFrom(change.getClass()) && changeRule.supports(change)) {
                final List<RuleConfig> configs = config.forRule(changeRule.getName());
                final String ruleName = changeRule.getName();

                for (RuleConfig ruleConfig : configs) {
                    if (isEnabled(ruleConfig) && ConditionHelper.evaluateCondition(ruleConfig, change)) {
                        changeRule.configure(ruleConfig);
                        final String message = changeRule.getMessage(change);

                        if (changeRule.invalid(change)) {
                            handleViolation(changeLog, changeSet, ruleName, message);
                        } else {
                            reportItems.add(ReportItem.passed(changeLog, changeSet, ruleName, message));
                        }
                    }
                }
            }
        }
    }

    public void checkChangeSet(ChangeSet changeSet) throws ChangeLogLintingException {
        final DatabaseChangeLog changeLog = changeSet.getChangeLog();

        for (ChangeSetRule changeSetRule : changeSetRules) {
            final String ruleName = changeSetRule.getName();
            final List<RuleConfig> configs = config.forRule(ruleName);
            for (RuleConfig ruleConfig : configs) {
                if (isEnabled(ruleConfig) && ConditionHelper.evaluateCondition(ruleConfig, changeSet)) {
                    changeSetRule.configure(ruleConfig);
                    final String message = changeSetRule.getMessage(changeSet);

                    if (changeSetRule.invalid(changeSet)) {
                        handleViolation(changeLog, changeSet, ruleName, message);
                    } else {
                        reportItems.add(ReportItem.passed(changeLog, changeSet, ruleName, message));
                    }
                }
            }
        }
    }

    public void checkChangeLog(DatabaseChangeLog changeLog) throws ChangeLogLintingException {
        for (ChangeLogRule changeLogRule : changeLogRules) {
            final String ruleName = changeLogRule.getName();
            final List<RuleConfig> configs = config.forRule(ruleName);
            for (RuleConfig ruleConfig : configs) {
                if (isEnabled(ruleConfig) && ConditionHelper.evaluateCondition(ruleConfig, changeLog)) {
                    changeLogRule.configure(ruleConfig);
                    final String message = changeLogRule.getMessage(changeLog);

                    if (changeLogRule.invalid(changeLog)) {
                        handleViolation(changeLog, null, ruleName, message);
                    } else {
                        reportItems.add(ReportItem.passed(changeLog, null, ruleName, message));
                    }
                }
            }
        }
    }

    private void handleViolation(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet, String rule, String message) throws ChangeLogLintingException {
        if (isIgnored(rule, changeSet)) {
            reportItems.add(ReportItem.ignored(databaseChangeLog, changeSet, rule, message));
        } else if (config.isFailFast()) {
            throw ChangeLogLintingException.from(databaseChangeLog, changeSet, message);
        } else {
            reportItems.add(ReportItem.error(databaseChangeLog, changeSet, rule, message));
        }
    }

    private boolean isIgnored(String ruleName, ChangeSet changeSet) {
        final String comments = Optional.ofNullable(changeSet).map(ChangeSet::getComments).orElse("");
        if (comments.endsWith(LQL_IGNORE_TOKEN)) {
            return true;
        }
        int index = comments.indexOf(LQL_IGNORE_TOKEN + ':'); // see if this specific rule is ignored
        if (index >= 0) {
            final String toIgnore = comments.substring(index + LQL_IGNORE_TOKEN.length() + 1);
            return Arrays.stream(toIgnore.split(",")).anyMatch(ruleName::equalsIgnoreCase);
        }
        return false;
    }

    private boolean isEnabled(RuleConfig ruleConfig) {
        return ruleConfig.isEnabled()
            && (Strings.isNullOrEmpty(ruleConfig.getEnableAfter()) || filesParsed.contains(ruleConfig.getEnableAfter()));
    }

    public void checkDuplicateIncludes(DatabaseChangeLog changeLog) throws ChangeLogLintingException {
        if (filesParsed.contains(changeLog.getPhysicalFilePath())) {
            for (RuleConfig ruleConfig : config.getEnabledRuleConfig("no-duplicate-includes")) {
                final String errorMessage = Optional.ofNullable(ruleConfig.getErrorMessage()).orElse("Changelog file '%s' was included more than once");
                throw new ChangeLogLintingException(String.format(errorMessage, changeLog));
            }
        }
    }
}
