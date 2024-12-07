package io.github.liquibaselinter.integration;

import static org.assertj.core.api.Assertions.*;

import io.github.liquibaselinter.ChangeLogLinter;
import io.github.liquibaselinter.ChangeLogLintingException;
import io.github.liquibaselinter.config.Config;
import io.github.liquibaselinter.config.ConfigLoader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import liquibase.Liquibase;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.DatabaseConnection;
import liquibase.database.OfflineConnection;
import liquibase.exception.LiquibaseException;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

abstract class LinterIntegrationTest {

    private static final ResourceAccessor TEST_FILES_RESOURCE_ACCESSOR = testFilesResourceAccessor();

    private final List<IntegrationTestConfig> tests = new ArrayList<>();

    abstract void registerTests();

    @TestFactory
    Stream<DynamicTest> dynamicTests() {
        registerTests();
        ThrowingConsumer<IntegrationTestConfig> testExecutor = running -> {
            DatabaseChangeLog databaseChangeLog = loadDatabaseChangelog(running.getChangeLogFile());
            ChangeLogLinter linter = initLinterFromConfigFile(running.getConfigFile());

            Throwable thrown = catchThrowable(() -> linter.lintChangeLog(databaseChangeLog));
            if (running.getMessage() != null) {
                assertThat(thrown)
                    .isInstanceOf(ChangeLogLintingException.class)
                    .hasMessageContaining(running.getMessage());
            } else {
                assertThat(thrown).isNull();
            }
        };
        return DynamicTest.stream(tests.iterator(), IntegrationTestConfig::getDisplayName, testExecutor);
    }

    private static ChangeLogLinter initLinterFromConfigFile(String configFile) throws IOException {
        Config config = ConfigLoader.loadConfig(TEST_FILES_RESOURCE_ACCESSOR, configFile);
        return new ChangeLogLinter(TEST_FILES_RESOURCE_ACCESSOR, config);
    }

    private static DatabaseChangeLog loadDatabaseChangelog(String changeLogFile) throws LiquibaseException {
        DatabaseConnection conn = new OfflineConnection("offline:h2", TEST_FILES_RESOURCE_ACCESSOR);
        Liquibase liquibase = new Liquibase(changeLogFile, TEST_FILES_RESOURCE_ACCESSOR, conn);
        return liquibase.getDatabaseChangeLog();
    }

    private static ResourceAccessor testFilesResourceAccessor() {
        try {
            return new DirectoryResourceAccessor(new File("src/test/resources/integration/"));
        } catch (FileNotFoundException exception) {
            throw new AssertionError("Failed to find integration test resources", exception);
        }
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

    private static final class IntegrationTestConfig {

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
