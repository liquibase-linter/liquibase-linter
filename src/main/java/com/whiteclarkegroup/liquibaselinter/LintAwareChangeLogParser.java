package com.whiteclarkegroup.liquibaselinter;

import com.google.auto.service.AutoService;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.google.common.io.Files;
import com.whiteclarkegroup.liquibaselinter.config.Config;
import com.whiteclarkegroup.liquibaselinter.config.ConfigLoader;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleConfig;
import com.whiteclarkegroup.liquibaselinter.config.rules.RuleRunner;
import com.whiteclarkegroup.liquibaselinter.report.Report;
import com.whiteclarkegroup.liquibaselinter.report.ReportItem;
import liquibase.Scope;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.ResourceAccessor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toSet;

@SuppressWarnings("WeakerAccess")
@AutoService(ChangeLogParser.class)
public class LintAwareChangeLogParser implements ChangeLogParser {
    protected final ConfigLoader configLoader = new ConfigLoader();
    private final ChangeLogLinter changeLogLinter = new ChangeLogLinter();
    private final ThreadLocal<LintingContext> context = new ThreadLocal<>();

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
        final boolean isRootChangeLog = context.get() == null;
        if (isRootChangeLog) {
            this.context.set(new LintingContext(configLoader.load(resourceAccessor)));
        }
        try {
            final LintingContext linting = this.context.get();
            final DatabaseChangeLog changeLog = parseChangeLog(physicalChangeLogLocation, changeLogParameters, resourceAccessor);
            final RuleRunner ruleRunner = new RuleRunner(linting.config, linting.reportItems, linting.filesParsed);

            if (!changeLog.getChangeSets().isEmpty()) {
                checkDuplicateIncludes(linting, physicalChangeLogLocation);
            }
            changeLogLinter.lintChangeLog(changeLog, linting.config, ruleRunner);
            linting.filesParsed.add(physicalChangeLogLocation);

            if (isRootChangeLog) {
                checkForFilesNotIncluded(linting, resourceAccessor);
                runReports(linting, ruleRunner.buildReport());
                final long errorCount = linting.reportItems.stream().filter(item -> item.getType() == ReportItem.ReportItemType.ERROR).count();
                if (errorCount > 0) {
                    throw new ChangeLogParseException(String.format("Linting failed with %d errors", errorCount));
                }
            }
            return changeLog;

        } finally {
            if (isRootChangeLog) {
                this.context.set(null);
            }
        }
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

    private static void checkForFilesNotIncluded(LintingContext linting, ResourceAccessor resourceAccessor) throws ChangeLogParseException {
        final Set<String> fileExtensions = linting.filesParsed.stream()
            .map(file -> Files.getFileExtension(file))
            .filter(ext -> !Strings.isNullOrEmpty(ext))
            .collect(toSet());

        for (RuleConfig ruleConfig : linting.config.getEnabledRuleConfig("file-not-included")) {
            List<String> paths = Optional.ofNullable(ruleConfig.getValues())
                .orElseThrow(() -> new IllegalArgumentException("values not configured for rule `file-not-included`"));

            for (String path : paths) {
                try {
                    final String unparsedFiles = resourceAccessor.list(null, path, true, true, false).stream()
                        .filter(file -> fileExtensions.contains(Files.getFileExtension(file)))
                        .filter(file -> !linting.filesParsed.contains(file))
                        .collect(joining(","));
                    if (!Strings.isNullOrEmpty(unparsedFiles)) {
                        final String errorMessage = Optional.ofNullable(ruleConfig.getErrorMessage())
                            .orElse("Changelog files not included in deltas change log: %s");
                        throw new ChangeLogParseException(String.format(errorMessage, unparsedFiles));
                    }
                } catch (IOException e) {
                    Scope.getCurrentScope().getLog(LintAwareChangeLogParser.class).warning("Cannot list files in " + path, e);
                }
            }
        }
    }

    private static void checkDuplicateIncludes(LintingContext linting, String physicalChangeLogLocation) throws ChangeLogParseException {
        if (linting.filesParsed.contains(physicalChangeLogLocation)) {
            for (RuleConfig ruleConfig : linting.config.getEnabledRuleConfig("no-duplicate-includes")) {
                final String errorMessage = Optional.ofNullable(ruleConfig.getErrorMessage()).orElse("Changelog file '%s' was included more than once");
                throw new ChangeLogParseException(String.format(errorMessage, physicalChangeLogLocation));
            }
        }
    }

    private static void runReports(LintingContext linting, Report report) throws ChangeLogParseException {
        linting.config.getReporting().forEach((reportType, reporter) -> {
            if (reporter.getConfiguration().isEnabled()) {
                reporter.processReport(report);
            }
        });
    }

    private static class LintingContext {
        final Set<String> filesParsed = Sets.newConcurrentHashSet();
        final List<ReportItem> reportItems = new ArrayList<>();
        final Config config;

        LintingContext(Config config) {
            this.config = config;
        }
    }
}
