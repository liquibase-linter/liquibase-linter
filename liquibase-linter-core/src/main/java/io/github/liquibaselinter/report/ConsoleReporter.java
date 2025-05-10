package io.github.liquibaselinter.report;

import com.google.auto.service.AutoService;
import java.io.PrintWriter;
import java.util.List;

public class ConsoleReporter extends TextReporter {

    public static final String NAME = "console";

    public ConsoleReporter(ReporterConfig config) {
        super(config);
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    protected void process(Report report, List<ReportItem> items) {
        PrintWriter writer = new PrintWriter(System.out);
        printReport(writer, report, items);
        writer.flush();
    }

    @Override
    protected void printItemTypeHeader(PrintWriter output, ReportItem.ReportItemType type) {
        printItemTypeName(output, type);
        output.println();
    }

    @Override
    protected void printItemTypeSummary(PrintWriter output, ReportItem.ReportItemType type, List<ReportItem> items) {
        output.append('\t');
        if (items.isEmpty()) {
            // don't draw attention with color when there are no report items
            output.print(type.name());
        } else {
            printItemTypeName(output, type);
        }
        output.append(": ").println(items.size());
    }

    protected void printItemTypeName(PrintWriter output, ReportItem.ReportItemType type) {
        output.print(type.name());
    }

    @Override
    protected void printSummaryDisabledRules(PrintWriter output, Report report) {
        output.append('\t');
        output.print("DISABLED");
        output.append(": ").println(countDisabledRules(report));
    }

    @AutoService(Reporter.Factory.class)
    public static class Factory extends AbstractReporter.Factory<ConsoleReporter> {

        public Factory() {
            super(NAME);
        }
    }
}
