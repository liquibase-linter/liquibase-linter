package io.github.liquibaselinter.report;

import static io.github.liquibaselinter.report.ReportsFixture.fullReport;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.nio.file.Path;
import liquibase.exception.UnexpectedLiquibaseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import tools.jackson.databind.annotation.JsonDeserialize;

class AbstractReporterTest {

    @Test
    void shouldFailWhenReportCannotBeWritten(@TempDir Path directory) {
        TextReporter reporter = new TextReporter(ReporterConfig.builder().withPath(directory.toString()).build());

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> reporter.processReport(fullReport()))
            .withMessage("Unable to write text report");
    }

    @Test
    void shouldFailWhenConfigHasNoBuilder() {
        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> new AbstractReporter.BaseFactory<>("test", TextReporter.class, NoBuilderConfig.class) {})
            .withMessageStartingWith("Cannot find builder for ");
    }

    @Test
    void shouldFailWhenReporterCannotBeInstantiated() {
        AbstractReporter.Factory<AbstractReporter> factory = new AbstractReporter.Factory<>(
            "test",
            AbstractReporter.class
        );

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> factory.create(true))
            .withMessageStartingWith("Cannot instantiate reporter of type ");
    }

    @Test
    void shouldFailWhenConfigBuilderCannotBeInstantiated() {
        AbstractReporter.BaseFactory<TextReporter, AbstractBuilderConfig> factory = new AbstractReporter.BaseFactory<>(
            "test",
            TextReporter.class,
            AbstractBuilderConfig.class
        ) {};

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> factory.create(true))
            .withMessageStartingWith("Cannot instantiate builder for ");
    }

    static class NoBuilderConfig extends ReporterConfig {

        NoBuilderConfig(ReporterConfig.BaseBuilder<?> builder) {
            super(builder);
        }
    }

    @JsonDeserialize(builder = ReporterConfig.BaseBuilder.class)
    static class AbstractBuilderConfig extends ReporterConfig {

        AbstractBuilderConfig(ReporterConfig.BaseBuilder<?> builder) {
            super(builder);
        }
    }
}
