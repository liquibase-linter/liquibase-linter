package io.github.liquibaselinter.report;

import static io.github.liquibaselinter.report.ReportItem.ReportItemType.ERROR;
import static io.github.liquibaselinter.report.ReportsFixture.fullReport;
import static org.approvaltests.Approvals.verify;

import io.github.liquibaselinter.report.ReportItem.ReportItemType;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.fusesource.jansi.io.HtmlAnsiOutputStream;
import org.junit.jupiter.api.Test;

class ConsoleReporterTest {

    @Test
    void emptyReport() {
        ReporterConfig reporterConfig = ReporterConfig.defaultConfig();

        String output = processReportWithConfig(ReportsFixture.emptyReport(), reporterConfig);

        verify(output);
    }

    @Test
    void defaultFilters() {
        ReporterConfig reporterConfig = ReporterConfig.defaultConfig();

        String output = processReportWithConfig(fullReport(), reporterConfig);

        verify(output);
    }

    @Test
    void errorFilter() {
        ReporterConfig reporterConfig = ReporterConfig.builder().withFilter(ERROR).build();

        String output = processReportWithConfig(fullReport(), reporterConfig);

        verify(output);
    }

    @Test
    void allFilters() {
        ReporterConfig reporterConfig = ReporterConfig.builder().withFilter(ReportItemType.values()).build();

        String output = processReportWithConfig(fullReport(), reporterConfig);

        verify(output);
    }

    private String processReportWithConfig(Report report, ReporterConfig reporterConfig) {
        PrintStream originalOut = System.out;
        try {
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            PrintStream out = new PrintStream(new HtmlAnsiOutputStream(outContent));
            System.setOut(out);
            new TestConsoleReporter(reporterConfig).processReport(report);
            return outContent.toString();
        } finally {
            System.setOut(originalOut);
        }
    }

    private static class TestConsoleReporter extends ConsoleReporter {

        TestConsoleReporter(ReporterConfig config) {
            super(config);
        }

        @Override
        protected void installAnsi() {
            // ansi is difficult to test with so noop installing makes it easier
        }

        @Override
        protected void uninstallAnsi() {
            // ansi is difficult to test with so noop uninstalling makes it easier
        }
    }
}
