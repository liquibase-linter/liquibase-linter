package io.github.liquibaselinter.report;

import static io.github.liquibaselinter.report.ReportItem.ReportItemType.ERROR;
import static io.github.liquibaselinter.report.ReporterConfig.builder;
import static io.github.liquibaselinter.report.ReportsFixture.emptyReport;
import static io.github.liquibaselinter.report.ReportsFixture.fullReport;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import io.github.liquibaselinter.report.ReportItem.ReportItemType;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

class ReporterTest {

    private static final Map<String, String> REPORT_TYPES = ImmutableMap.of(
        TextReporter.NAME,
        "txt",
        MarkdownReporter.NAME,
        "md"
    );

    final List<ReportingTestConfig> tests = new ArrayList<>();

    @TestFactory
    Stream<DynamicTest> reporterTests() {
        REPORT_TYPES.forEach((reportType, suffix) -> {
            addDefaultFilters(reportType, suffix);
            addLimitedFilters(reportType, suffix);
            addFullFilters(reportType, suffix);
            addEmptyReport(reportType, suffix);
        });

        ThrowingConsumer<ReportingTestConfig> testExecutor = running -> {
            String output = runFileReport(running);
            assertThat(output).isEqualTo(running.getExpectedOutput());
        };
        return DynamicTest.stream(tests.iterator(), ReportingTestConfig::getDisplayName, testExecutor);
    }

    private String runFileReport(ReportingTestConfig running) throws IOException {
        for (Reporter.Factory factory : ServiceLoader.load(Reporter.Factory.class)) {
            if (factory.supports(running.reportType)) {
                factory.create(running.config).processReport(running.report);
            }
        }
        String output;
        output = Files.asCharSource(new File(running.outputPath), StandardCharsets.UTF_8).read();
        return output;
    }

    private void addDefaultFilters(String reportType, String suffix) {
        tests.add(
            new ReportingTestConfig(
                reportType,
                builder().build(),
                fullReport(),
                "target/lqlint-report." + suffix,
                "defaultFilters." + suffix
            )
        );
        tests.add(
            new ReportingTestConfig(
                reportType,
                builder().withPath("target/lqlint-custom." + suffix).build(),
                fullReport(),
                "target/lqlint-custom." + suffix,
                "defaultFilters." + suffix
            )
        );
    }

    private void addLimitedFilters(String reportType, String suffix) {
        tests.add(
            new ReportingTestConfig(
                reportType,
                builder().withFilter(ERROR).build(),
                fullReport(),
                "target/lqlint-report." + suffix,
                "limitedFilters." + suffix
            )
        );
        tests.add(
            new ReportingTestConfig(
                reportType,
                builder().withFilter(ERROR).withPath("target/lqlint-custom." + suffix).build(),
                fullReport(),
                "target/lqlint-custom." + suffix,
                "limitedFilters." + suffix
            )
        );
    }

    private void addFullFilters(String reportType, String suffix) {
        tests.add(
            new ReportingTestConfig(
                reportType,
                builder().withFilter(ReportItemType.values()).build(),
                fullReport(),
                "target/lqlint-report." + suffix,
                "fullFilters." + suffix
            )
        );
        tests.add(
            new ReportingTestConfig(
                reportType,
                builder().withFilter(ReportItemType.values()).withPath("target/lqlint-custom." + suffix).build(),
                fullReport(),
                "target/lqlint-custom." + suffix,
                "fullFilters." + suffix
            )
        );
    }

    private void addEmptyReport(String reportType, String suffix) {
        tests.add(
            new ReportingTestConfig(
                reportType,
                builder().build(),
                emptyReport(),
                "target/lqlint-report." + suffix,
                "emptyReport." + suffix
            )
        );
        tests.add(
            new ReportingTestConfig(
                reportType,
                builder().withPath("target/lqlint-custom." + suffix).build(),
                emptyReport(),
                "target/lqlint-custom." + suffix,
                "emptyReport." + suffix
            )
        );
    }

    private static final class ReportingTestConfig {

        final String reportType;
        final ReporterConfig config;
        final Report report;
        final String outputPath;
        final String expectedResourceName;

        private ReportingTestConfig(
            String reportType,
            ReporterConfig config,
            Report report,
            String outputPath,
            String expectedResourceName
        ) {
            this.reportType = reportType;
            this.config = config;
            this.report = report;
            this.outputPath = outputPath;
            this.expectedResourceName = expectedResourceName;
        }

        public String getDisplayName() {
            return "Should produce " + outputPath + " for " + reportType + " matching " + expectedResourceName;
        }

        public String getExpectedOutput() throws IOException {
            final URL expectedOutputUrl = Resources.getResource("reports/" + expectedResourceName);
            return Resources.toString(expectedOutputUrl, StandardCharsets.UTF_8);
        }
    }
}
