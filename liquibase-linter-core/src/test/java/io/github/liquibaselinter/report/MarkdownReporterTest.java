package io.github.liquibaselinter.report;

import static io.github.liquibaselinter.report.ReportItem.ReportItemType.ERROR;
import static io.github.liquibaselinter.report.ReportsFixture.fullReport;
import static org.approvaltests.Approvals.verify;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.report.ReportItem.ReportItemType;
import java.io.File;
import org.junit.jupiter.api.Test;

class MarkdownReporterTest {

    private final FileReporterTestHelper reporterTester = new FileReporterTestHelper(MarkdownReporter.NAME, "md");

    @Test
    void useSpecifiedPath() {
        ReporterConfig reporterConfig = ReporterConfig.builder().withPath("target/lqlint-report.md").build();

        File outputFile = reporterTester.processReportWithConfig(fullReport(), reporterConfig);

        assertThat(outputFile).exists().isNotEmpty();
        assertThat(outputFile.getPath()).isEqualTo("target/lqlint-report.md");
    }

    @Test
    void emptyReport() {
        ReporterConfig reporterConfig = ReporterConfig.defaultConfig();

        File outputFile = reporterTester.processReportWithConfig(ReportsFixture.emptyReport(), reporterConfig);

        verify(outputFile);
    }

    @Test
    void fullReportWithDefaultFilters() {
        ReporterConfig reporterConfig = ReporterConfig.defaultConfig();

        File outputFile = reporterTester.processReportWithConfig(fullReport(), reporterConfig);

        verify(outputFile);
    }

    @Test
    void fullReportWithErrorFilter() {
        ReporterConfig reporterConfig = ReporterConfig.builder().withFilter(ERROR).build();

        File outputFile = reporterTester.processReportWithConfig(fullReport(), reporterConfig);

        verify(outputFile);
    }

    @Test
    void fullReportWithAllFilters() {
        ReporterConfig reporterConfig = ReporterConfig.builder().withFilter(ReportItemType.values()).build();

        File outputFile = reporterTester.processReportWithConfig(fullReport(), reporterConfig);

        verify(outputFile);
    }
}
