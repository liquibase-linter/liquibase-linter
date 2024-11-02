package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.rules.core.FileNameNoSpacesRule;
import liquibase.changelog.DatabaseChangeLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileNameNoSpacesRuleTest {

    private FileNameNoSpacesRule fileNameNoSpaces;

    @BeforeEach
    void setUp() {
        fileNameNoSpaces = new FileNameNoSpacesRule();
    }

    @DisplayName("Filename with spaces should be invalid")
    @Test
    void filenameWithSpacesShouldBeInvalid() {
        assertTrue(fileNameNoSpaces.invalid(withFilePath("//test/dir/this has spaces.xml")));
    }

    @DisplayName("Filename without spaces should be valid")
    @Test
    void filenameWithoutSpacesShouldBeValid() {
        assertFalse(fileNameNoSpaces.invalid(withFilePath("//test/dir/this-has-spaces.xml")));
    }

    private DatabaseChangeLog withFilePath(String filePath) {
        final DatabaseChangeLog databaseChangeLog = new DatabaseChangeLog();
        databaseChangeLog.setLogicalFilePath(filePath);
        return databaseChangeLog;
    }
}
