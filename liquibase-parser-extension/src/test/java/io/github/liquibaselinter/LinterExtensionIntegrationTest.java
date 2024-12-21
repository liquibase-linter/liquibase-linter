package io.github.liquibaselinter;

import static org.assertj.core.api.Assertions.*;

import com.google.common.io.CharStreams;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Writer;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.OfflineConnection;
import liquibase.exception.CommandExecutionException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class LinterExtensionIntegrationTest {

    private static final ResourceAccessor TEST_FILES_RESOURCE_ACCESSOR = testFilesResourceAccessor();
    private static final String LQLINT_CONFIG_PROPERTY = "lqlint.config.path";

    @AfterEach
    void tearDown() {
        System.clearProperty(LQLINT_CONFIG_PROPERTY);
    }

    @Test
    void extensionShouldRegisterCorrectly() throws LiquibaseException {
        Writer nullWriter = CharStreams.nullWriter();
        Contexts contexts = new Contexts();

        System.setProperty(LQLINT_CONFIG_PROPERTY, "src/test/resources/lqlint.json");

        Liquibase liquibase = createLiquibaseWithChangelog("master.xml");

        assertThatExceptionOfType(CommandExecutionException.class)
            .isThrownBy(() -> liquibase.update(contexts, nullWriter))
            .havingRootCause()
            .isInstanceOf(ChangeLogLintingException.class)
            .withMessageContaining("Linting failed with 1 errors");
    }

    private static Liquibase createLiquibaseWithChangelog(String changeLogFile) throws LiquibaseException {
        DatabaseConnection conn = new OfflineConnection("offline:h2", TEST_FILES_RESOURCE_ACCESSOR);
        return new Liquibase(changeLogFile, TEST_FILES_RESOURCE_ACCESSOR, conn);
    }

    private static ResourceAccessor testFilesResourceAccessor() {
        try {
            return new DirectoryResourceAccessor(new File("src/test/resources/"));
        } catch (FileNotFoundException exception) {
            throw new AssertionError("Failed to find integration test resources", exception);
        }
    }
}
