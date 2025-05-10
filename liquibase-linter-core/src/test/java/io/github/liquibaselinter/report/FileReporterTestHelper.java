package io.github.liquibaselinter.report;

import java.io.File;
import java.util.Optional;
import java.util.ServiceLoader;

public class FileReporterTestHelper {

    private final String reporterName;
    private final String expectedFileSuffix;

    public FileReporterTestHelper(String reporterName, String expectedFileSuffix) {
        this.reporterName = reporterName;
        this.expectedFileSuffix = expectedFileSuffix;
    }

    public File processReportWithConfig(Report report, ReporterConfig reporterConfig) {
        Reporter reporter = FileReporterTestHelper.findFactory(reporterName).create(reporterConfig);
        reporter.processReport(report);

        String outputPath = Optional.ofNullable(reporterConfig.getPath()).orElse(
            String.format("target/lqlint-report.%s", expectedFileSuffix)
        );
        return new File(outputPath);
    }

    @SuppressWarnings("unchecked")
    static Reporter.Factory<? super Reporter, ? super ReporterConfig> findFactory(String reporterName) {
        for (Reporter.Factory<? super Reporter, ? super ReporterConfig> factory : ServiceLoader.load(
            Reporter.Factory.class
        )) {
            if (factory.supports(reporterName)) {
                return factory;
            }
        }
        throw new IllegalArgumentException("No factory found for reporter: " + reporterName);
    }
}
