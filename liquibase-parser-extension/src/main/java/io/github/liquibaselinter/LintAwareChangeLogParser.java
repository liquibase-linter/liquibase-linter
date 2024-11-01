package io.github.liquibaselinter;

import com.google.auto.service.AutoService;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.ChangeLogParseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.ResourceAccessor;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

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
                new ChangeLogLinter(resourceAccessor).lintChangeLog(changeLog);
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
}
