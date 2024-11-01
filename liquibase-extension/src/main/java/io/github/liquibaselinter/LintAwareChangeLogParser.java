package io.github.liquibaselinter;

import com.google.auto.service.AutoService;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import io.github.liquibaselinter.config.Config;
import io.github.liquibaselinter.config.ConfigLoader;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.report.Report;
import io.github.liquibaselinter.report.ReportItem;
import liquibase.Scope;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.ResourceAccessor;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

@SuppressWarnings("WeakerAccess")
@AutoService(ChangeLogParser.class)
public class LintAwareChangeLogParser implements ChangeLogParser {

    @Override
    public boolean supports(String changeLogFile, ResourceAccessor resourceAccessor) {
        return getParsers().anyMatch(parser -> parser.supports(changeLogFile, resourceAccessor));
    }

    @Override
    public int getPriority() {
        return 100;
    }

    @Override
    public DatabaseChangeLog parse(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
        try {
            final DatabaseChangeLog changeLog = parseChangeLog(physicalChangeLogLocation, changeLogParameters, resourceAccessor);

            if (isRootChangeLog(changeLog)) {
                Config config = new ConfigLoader().load(resourceAccessor);

                ChangeLogLinter changeLogLinter = new ChangeLogLinter(config);
                changeLogLinter.lintChangeLog(changeLog);

                checkForFilesNotIncluded(resourceAccessor, changeLogLinter.getFilesParsed(), config);

                runReports(changeLogLinter.report(), config);
                final long errorCount = changeLogLinter.report().getItems().stream().filter(item -> item.getType() == ReportItem.ReportItemType.ERROR).count();
                if (errorCount > 0) {
                    throw new ChangeLogLintingException(String.format("Linting failed with %d errors", errorCount));
                }
            }
            return changeLog;

        } catch (ChangeLogLintingException lintingException) {
            throw new ChangeLogParseException(lintingException.getMessage(), lintingException);
        }
    }

    private static boolean isRootChangeLog(DatabaseChangeLog changeLog) {
        return changeLog.getRootChangeLog() == changeLog;
    }

    private static DatabaseChangeLog parseChangeLog(String physicalChangeLogLocation, ChangeLogParameters changeLogParameters, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
        ChangeLogParser supportingParser = getParsers()
            .filter(parser -> parser.supports(physicalChangeLogLocation, resourceAccessor))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Change log file type not supported"));
        return supportingParser.parse(physicalChangeLogLocation, changeLogParameters, resourceAccessor);
    }

    private static Stream<ChangeLogParser> getParsers() {
        return ChangeLogParserFactory.getInstance().getParsers()
            .stream()
            .filter(parser -> !(parser instanceof LintAwareChangeLogParser));
    }

    private static void checkForFilesNotIncluded(ResourceAccessor resourceAccessor, Set<String> filesParsed, Config config) throws ChangeLogLintingException {
        final Set<String> fileExtensions = filesParsed.stream()
            .map(Files::getFileExtension)
            .filter(ext -> !Strings.isNullOrEmpty(ext))
            .collect(toSet());

        for (RuleConfig ruleConfig : config.getEnabledRuleConfig("file-not-included")) {
            List<String> paths = Optional.ofNullable(ruleConfig.getValues())
                .orElseThrow(() -> new IllegalArgumentException("values not configured for rule `file-not-included`"));

            for (String path : paths) {
                try {
                    final String unparsedFiles = resourceAccessor.list(null, path, true, true, false).stream()
                        .filter(file -> fileExtensions.contains(Files.getFileExtension(file)))
                        .filter(file -> !filesParsed.contains(file))
                        .collect(joining(","));
                    if (!Strings.isNullOrEmpty(unparsedFiles)) {
                        final String errorMessage = Optional.ofNullable(ruleConfig.getErrorMessage())
                            .orElse("Changelog files not included in deltas change log: %s");
                        throw new ChangeLogLintingException(String.format(errorMessage, unparsedFiles));
                    }
                } catch (IOException e) {
                    Scope.getCurrentScope().getLog(LintAwareChangeLogParser.class).warning("Cannot list files in " + path, e);
                }
            }
        }
    }

    private static void runReports(Report report, Config config) {
        config.getReporting().forEach((reportType, reporter) -> {
            if (reporter.getConfiguration().isEnabled()) {
                reporter.processReport(report);
            }
        });
    }
}
