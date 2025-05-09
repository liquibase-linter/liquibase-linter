package io.github.liquibaselinter.report;

import static org.fusesource.jansi.Ansi.ansi;

import com.google.auto.service.AutoService;
import java.io.PrintWriter;
import java.util.List;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class ConsoleReporter extends TextReporter {

    public static final String NAME = "console";

    public ConsoleReporter(ReporterConfig config) {
        super(config);
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    protected void process(Report report, List<ReportItem> items) {
        installAnsi();
        PrintWriter writer = new PrintWriter(System.out);
        printReport(writer, report, items);
        writer.flush();
        uninstallAnsi();
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

    private void printItemTypeName(PrintWriter output, ReportItem.ReportItemType type) {
        switch (type) {
            case ERROR:
                printColoured(output, Ansi.Color.RED, type.name());
                break;
            case IGNORED:
                printColoured(output, Ansi.Color.YELLOW, type.name());
                break;
            case PASSED:
                printColoured(output, Ansi.Color.GREEN, type.name());
                break;
            default:
                super.printItemTypeHeader(output, type);
                break;
        }
    }

    private void printColoured(PrintWriter output, Ansi.Color colour, String line) {
        output.print(ansi().reset().fg(colour).a(line).reset().toString());
    }

    @Override
    protected void printSummaryDisabledRules(PrintWriter output, Report report) {
        long disabled = countDisabledRules(report);

        output.append('\t');
        if (disabled > 0) {
            output.print(ansi().reset().fg(Ansi.Color.MAGENTA).a("DISABLED").reset().toString());
        } else {
            // don't draw attention with color when there are no report items
            output.print("DISABLED");
        }
        output.append(": ").println(disabled);
    }

    protected void installAnsi() {
        AnsiConsole.systemInstall();
    }

    protected void uninstallAnsi() {
        AnsiConsole.systemUninstall();
    }

    @AutoService(Reporter.Factory.class)
    public static class Factory extends AbstractReporter.Factory<ConsoleReporter> {

        public Factory() {
            super(NAME);
        }
    }
}
