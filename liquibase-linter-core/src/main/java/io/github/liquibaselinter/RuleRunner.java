package io.github.liquibaselinter;

import static java.util.stream.Collectors.toList;

import io.github.liquibaselinter.config.Config;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.report.Report;
import io.github.liquibaselinter.report.ReportItem;
import io.github.liquibaselinter.rules.ChangeLogRule;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.ChangeSetRule;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.StreamSupport;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import org.apache.commons.lang3.StringUtils;

class RuleRunner {

    private static final String LQL_IGNORE_TOKEN = "lql-ignore";

    private final Config config;
    private final List<ChangeRule> changeRules = loadAvailablesServices(ChangeRule.class);
    private final List<ChangeSetRule> changeSetRules = loadAvailablesServices(ChangeSetRule.class);
    private final List<ChangeLogRule> changeLogRules = loadAvailablesServices(ChangeLogRule.class);
    private final List<ReportItem> reportItems = new ArrayList<>();
    private final Set<String> filesParsed = new HashSet<>();

    public RuleRunner(Config config) {
        this.config = config;
    }

    private static <T> List<T> loadAvailablesServices(Class<T> clazz) {
        return StreamSupport.stream(ServiceLoader.load(clazz).spliterator(), false).collect(toList());
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
            final List<RuleConfig> configs = config.forRule(changeRule.getName());
            final String ruleName = changeRule.getName();

            for (RuleConfig ruleConfig : configs) {
                if (isEnabled(ruleConfig) && ConditionHelper.evaluateCondition(ruleConfig, change)) {
                    Collection<RuleViolation> violations = changeRule.check(change, ruleConfig);
                    for (RuleViolation violation : violations) {
                        handleViolation(changeLog, changeSet, ruleName, violation.message());
                    }
                    if (violations.isEmpty()) {
                        reportItems.add(ReportItem.passed(changeLog, changeSet, ruleName, ""));
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
                    Collection<RuleViolation> violations = changeSetRule.check(changeSet, ruleConfig);
                    for (RuleViolation violation : violations) {
                        handleViolation(changeLog, changeSet, ruleName, violation.message());
                    }
                    if (violations.isEmpty()) {
                        reportItems.add(ReportItem.passed(changeLog, changeSet, ruleName, ""));
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
                    Collection<RuleViolation> ruleViolations = changeLogRule.check(changeLog, ruleConfig);
                    for (RuleViolation ruleViolation : ruleViolations) {
                        handleViolation(changeLog, null, ruleName, ruleViolation.message());
                    }
                    if (ruleViolations.isEmpty()) {
                        reportItems.add(ReportItem.passed(changeLog, null, ruleName, ""));
                    }
                }
            }
        }
    }

    private void handleViolation(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet, String rule, String message)
        throws ChangeLogLintingException {
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
        return (
            ruleConfig.isEnabled() &&
            (StringUtils.isEmpty(ruleConfig.getEnableAfter()) || filesParsed.contains(ruleConfig.getEnableAfter()))
        );
    }
}
