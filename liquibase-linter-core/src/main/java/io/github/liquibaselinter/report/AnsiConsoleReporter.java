package io.github.liquibaselinter.report;

import static org.fusesource.jansi.Ansi.ansi;

import com.google.auto.service.AutoService;
import java.io.PrintWriter;
import java.util.List;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class AnsiConsoleReporter extends ConsoleReporter {

    public static final String NAME = "ansi-console";

    public AnsiConsoleReporter(ReporterConfig config) {
        super(config);
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    protected void process(Report report, List<ReportItem> items) {
        installAnsi();
        super.process(report, items);
        uninstallAnsi();
    }

    @Override
    protected void printItemTypeName(PrintWriter output, ReportItem.ReportItemType type) {
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
                super.printItemTypeName(output, type);
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
            printColoured(output, Ansi.Color.MAGENTA, "DISABLED");
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
    public static class Factory extends AbstractReporter.Factory<AnsiConsoleReporter> {

        public Factory() {
            super(NAME);
        }
    }
}
