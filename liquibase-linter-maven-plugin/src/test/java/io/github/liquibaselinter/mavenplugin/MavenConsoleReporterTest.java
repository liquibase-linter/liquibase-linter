package io.github.liquibaselinter.mavenplugin;

import static io.github.liquibaselinter.report.ReportsFixture.emptyReport;
import static io.github.liquibaselinter.report.ReportsFixture.fullReport;
import static org.approvaltests.Approvals.verify;

import io.github.liquibaselinter.report.Report;
import org.junit.jupiter.api.Test;

class MavenConsoleReporterTest {

    @Test
    void withNoViolations() {
        String output = processReport(emptyReport());

        verify(output);
    }

    @Test
    void withViolations() {
        String output = processReport(fullReport());

        verify(output);
    }

    private String processReport(Report report) {
        CapturingLog logs = new CapturingLog();
        new MavenConsoleReporter(logs).processReport(report);
        return logs.capturedLogs();
    }
}
