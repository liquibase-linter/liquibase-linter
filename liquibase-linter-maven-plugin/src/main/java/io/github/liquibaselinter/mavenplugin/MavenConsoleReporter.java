package io.github.liquibaselinter.mavenplugin;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.groupingBy;
import static org.apache.commons.lang3.StringUtils.isEmpty;

import io.github.liquibaselinter.report.EmptyLastComparator;
import io.github.liquibaselinter.report.Report;
import io.github.liquibaselinter.report.ReportItem;
import io.github.liquibaselinter.report.Reporter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.maven.plugin.logging.Log;

class MavenConsoleReporter implements Reporter {

    private final Log log;

    public MavenConsoleReporter(Log log) {
        this.log = log;
    }

    @Override
    public void processReport(Report report) {
        Set<ReportItem> errors = report
            .getItems()
            .stream()
            .filter(reportItem -> reportItem.getType() == ReportItem.ReportItemType.ERROR)
            .collect(Collectors.toSet());
        if (errors.isEmpty()) {
            log.info("No violation found");
            return;
        }
        log.error(errors.size() + " violation(s) found");
        printByChangeLogFile(errors);
    }

    private void printByChangeLogFile(Collection<ReportItem> items) {
        items
            .stream()
            .collect(groupingBy(item -> ofNullable(item.getFilePath()).map(String::trim).orElse("")))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey(new EmptyLastComparator()))
            .forEach(entry -> printChangeLogFile(entry.getKey(), entry.getValue()));
    }

    private void printChangeLogFile(String fileName, List<ReportItem> items) {
        printChangeLogHeader(fileName);
        printByChangeSet(items);
    }

    private void printChangeLogHeader(String fileName) {
        if (isEmpty(fileName)) {
            println("Other");
        } else {
            println(fileName);
        }
    }

    private void printByChangeSet(List<ReportItem> items) {
        items
            .stream()
            .collect(groupingBy(item -> ofNullable(item.getChangeSetId()).map(String::trim).orElse("")))
            .entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> printChangeSet(entry.getKey(), entry.getValue()));
    }

    private void printChangeSet(String changeSetId, List<ReportItem> items) {
        printChangeSetHeader(changeSetId);
        items.forEach(this::printItemDetail);
    }

    private void printChangeSetHeader(String changeSetId) {
        if (!changeSetId.isEmpty()) {
            println("\t", "changeSet '", changeSetId, "'");
        }
    }

    private void printItemDetail(ReportItem item) {
        println("\t\t'", item.getRule(), "': ", indentMessage(item.getMessage()));
    }

    private String indentMessage(String message) {
        return message.replace("\n", "\n\t\t");
    }

    private void println(String... content) {
        log.error(String.join("", content));
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
