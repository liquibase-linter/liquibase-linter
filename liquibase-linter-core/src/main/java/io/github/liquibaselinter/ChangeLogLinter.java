package io.github.liquibaselinter;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

import com.google.common.base.Strings;
import com.google.common.io.Files;
import io.github.liquibaselinter.config.Config;
import io.github.liquibaselinter.config.ConfigLoader;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.report.ReportItem;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import liquibase.ContextExpression;
import liquibase.Scope;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.resource.Resource;
import liquibase.resource.ResourceAccessor;
import org.apache.commons.lang3.StringUtils;

public class ChangeLogLinter {

    private final Config config;
    private final RuleRunner ruleRunner;
    private final ResourceAccessor resourceAccessor;

    public ChangeLogLinter(ResourceAccessor resourceAccessor, Config config) {
        Objects.requireNonNull(resourceAccessor, "ResourceAccessor must not be null");
        Objects.requireNonNull(config, "Configuration must not be null");

        this.resourceAccessor = resourceAccessor;
        this.config = config;
        this.ruleRunner = new RuleRunner(this.config);
    }

    public ChangeLogLinter(ResourceAccessor resourceAccessor) {
        this(resourceAccessor, ConfigLoader.load(resourceAccessor));
    }

    public void lintChangeLog(final DatabaseChangeLog databaseChangeLog) throws ChangeLogLintingException {
        if (shouldLint(databaseChangeLog)) {
            ruleRunner.checkChangeLog(databaseChangeLog);
        }

        lintChangeSets(databaseChangeLog.getChangeSets());

        ruleRunner.getFilesParsed().add(databaseChangeLog.getPhysicalFilePath());

        checkForFilesNotIncluded();
        reports();
    }

    private void lintChangeSets(List<ChangeSet> changeSets) throws ChangeLogLintingException {
        for (ChangeSet changeSet : changeSets) {
            DatabaseChangeLog databaseChangeLog = changeSet.getChangeLog();
            if (shouldLint(databaseChangeLog) && isNotRootChangeLog(databaseChangeLog)) {
                ruleRunner.checkChangeLog(databaseChangeLog);
            }

            if (shouldLint(changeSet)) {
                ruleRunner.checkChangeSet(changeSet);

                for (Change change : changeSet.getChanges()) {
                    ruleRunner.checkChange(change);
                }
            }

            ruleRunner.getFilesParsed().add(databaseChangeLog.getPhysicalFilePath());
        }
    }

    private static boolean isNotRootChangeLog(DatabaseChangeLog databaseChangeLog) {
        return databaseChangeLog.getRootChangeLog() != databaseChangeLog;
    }

    private boolean shouldLint(DatabaseChangeLog changeLog) {
        return (
            isEnabled() &&
            isFilePathNotIgnored(changeLog.getFilePath()) &&
            !hasAlreadyBeenParsed(changeLog.getFilePath())
        );
    }

    private boolean isEnabled() {
        return StringUtils.isEmpty(config.getEnableAfter()) || hasAlreadyBeenParsed(config.getEnableAfter());
    }

    private boolean hasAlreadyBeenParsed(String filePath) {
        return ruleRunner.getFilesParsed().contains(filePath);
    }

    private boolean shouldLint(ChangeSet changeSet) {
        return isEnabled() && !isContextIgnored(changeSet) && isFilePathNotIgnored(changeSet.getFilePath());
    }

    private boolean isContextIgnored(ChangeSet changeSet) {
        final Set<String> contexts = Optional.ofNullable(changeSet.getContextFilter())
            .map(ContextExpression::getContexts)
            .orElseGet(Collections::emptySet);
        if (config.getIgnoreContextPattern() != null && !contexts.isEmpty()) {
            return contexts.stream().anyMatch(context -> config.getIgnoreContextPattern().matcher(context).matches());
        }
        return false;
    }

    private boolean isFilePathNotIgnored(String filePath) {
        if (filePath != null && config.getIgnoreFilesPattern() != null) {
            String changeLogPath = filePath.replace('\\', '/');
            return !config.getIgnoreFilesPattern().matcher(changeLogPath).matches();
        }
        return true;
    }

    private void checkForFilesNotIncluded() throws ChangeLogLintingException {
        final Set<String> fileExtensions = ruleRunner
            .getFilesParsed()
            .stream()
            .map(Files::getFileExtension)
            .filter(ext -> !Strings.isNullOrEmpty(ext))
            .collect(toSet());

        for (RuleConfig ruleConfig : config.getEnabledRuleConfig("file-not-included")) {
            List<String> paths = Optional.ofNullable(ruleConfig.getValues()).orElseThrow(() ->
                new IllegalArgumentException("values not configured for rule `file-not-included`")
            );

            for (String path : paths) {
                try {
                    final String unparsedFiles = resourceAccessor
                        .search(path, true)
                        .stream()
                        .map(Resource::getPath)
                        .filter(file -> fileExtensions.contains(Files.getFileExtension(file)))
                        .filter(file -> !ruleRunner.getFilesParsed().contains(file))
                        .collect(joining(","));
                    if (!Strings.isNullOrEmpty(unparsedFiles)) {
                        final String errorMessage = Optional.ofNullable(ruleConfig.getErrorMessage()).orElse(
                            "Changelog files not included in deltas change log: %s"
                        );
                        throw new ChangeLogLintingException(String.format(errorMessage, unparsedFiles));
                    }
                } catch (IOException e) {
                    Scope.getCurrentScope().getLog(ChangeLogLinter.class).warning("Cannot list files in " + path, e);
                }
            }
        }
    }

    private void reports() throws ChangeLogLintingException {
        config
            .getReporting()
            .forEach((reportType, reporter) -> {
                if (reporter.isEnabled()) {
                    reporter.processReport(ruleRunner.buildReport());
                }
            });
        final List<ReportItem> errors = ruleRunner
            .buildReport()
            .getItems()
            .stream()
            .filter(item -> item.getType() == ReportItem.ReportItemType.ERROR)
            .collect(Collectors.toList());
        final long errorCount = errors.size();
        if (errorCount > 0) {
            final String errorList = errors.stream().map(ReportItem::getMessage).collect(joining("\n - ", "\n - ", ""));
            throw new ChangeLogLintingException(
                String.format("Linting failed with %d errors: %s", errorCount, errorList)
            );
        }
    }
}
