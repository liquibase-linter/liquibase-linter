package io.github.liquibaselinter.integration;

import com.google.common.io.CharStreams;
import io.github.liquibaselinter.ChangeLogLintingException;
import io.github.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.exception.ChangeLogParseException;
import liquibase.exception.CommandExecutionException;
import liquibase.parser.ChangeLogParserFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

abstract class LinterIntegrationTest {

    private final List<IntegrationTestConfig> tests = new ArrayList<>();

    abstract void registerTests();

    @TestFactory
    Stream<DynamicTest> dynamicTests() {
        registerTests();
        ThrowingConsumer<IntegrationTestConfig> testExecutor = running -> {
            final Liquibase liquibase = LiquibaseIntegrationTestResolver.buildLiquibase(running.getChangeLogFile(), running.getConfigFile());
            final Writer nullWriter = CharStreams.nullWriter();
            final Contexts contexts = new Contexts();
            if (running.getMessage() != null) {
                assertThatExceptionOfType(CommandExecutionException.class)
                    .isThrownBy(() -> liquibase.update(contexts, nullWriter))
                    .havingRootCause()
                    .isInstanceOf(ChangeLogLintingException.class)
                    .withMessageContaining(running.getMessage());
            } else {
                liquibase.update(contexts, nullWriter);
            }
        };
        return DynamicTest.stream(tests.iterator(), IntegrationTestConfig::getDisplayName, testExecutor);
    }

    @AfterEach
    void tearDown() {
        ChangeLogParserFactory.reset();
    }

    protected void shouldFail(String displayName, String changeLogFile, String configFile, String message) {
        tests.add(new IntegrationTestConfig(displayName, changeLogFile, configFile, message));
    }

    protected void shouldPass(String displayName, String changeLogFile, String configFile) {
        tests.add(new IntegrationTestConfig(displayName, changeLogFile, configFile, null));
    }

    private static class IntegrationTestConfig {
        private final String displayName;
        private final String changeLogFile;
        private final String configFile;
        private final String message;

        private IntegrationTestConfig(String displayName, String changeLogFile, String configFile, String message) {
            this.displayName = displayName;
            this.changeLogFile = changeLogFile;
            this.configFile = configFile;
            this.message = message;
        }

        private String getDisplayName() {
            return displayName;
        }

        private String getChangeLogFile() {
            return changeLogFile;
        }

        private String getConfigFile() {
            return configFile;
        }

        private String getMessage() {
            return message;
        }
    }
}
