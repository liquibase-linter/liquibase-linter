package io.github.liquibaselinter.gradle.internal;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;

import io.github.liquibaselinter.report.EmptyLastComparator;
import io.github.liquibaselinter.report.Report;
import io.github.liquibaselinter.report.ReportItem;
import io.github.liquibaselinter.report.Reporter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.gradle.api.logging.Logger;

/**
 * Prints the violations in the Gradle build output, grouped by changelog file and changeset.
 */
class GradleLoggerReporter implements Reporter {

    private final Logger logger;

    GradleLoggerReporter(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void processReport(Report report) {
        List<ReportItem> errors = report
            .getItems()
            .stream()
            .filter(reportItem -> reportItem.type() == ReportItem.ReportItemType.ERROR)
            .toList();
        if (errors.isEmpty()) {
            logger.info("No violation found");
            return;
        }
        logger.error("{} violation(s) found", errors.size());
        errors
            .stream()
            .collect(groupingBy(item -> ofNullable(item.filePath()).map(String::trim).orElse("")))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey(new EmptyLastComparator()))
            .forEach(entry -> printChangeLogFile(entry.getKey(), entry.getValue()));
    }

    private void printChangeLogFile(String fileName, List<ReportItem> items) {
        logger.error(fileName.isEmpty() ? "Other" : fileName);
        items
            .stream()
            .collect(groupingBy(item -> ofNullable(item.changeSetId()).map(String::trim).orElse("")))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> printChangeSet(entry.getKey(), entry.getValue()));
    }

    private void printChangeSet(String changeSetId, List<ReportItem> items) {
        if (!changeSetId.isEmpty()) {
            logger.error("\tchangeSet '{}'", changeSetId);
        }
        items
            .stream()
            .sorted(Comparator.comparing(ReportItem::rule).thenComparing(ReportItem::message))
            .forEach(item -> logger.error("\t\t'{}': {}", item.rule(), item.message().replace("\n", "\n\t\t")));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
