package io.github.liquibaselinter;

import static org.assertj.core.api.Assertions.assertThat;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import org.junit.jupiter.api.Test;

class ChangeLogLintingExceptionTest {

    @Test
    void shouldDescribeChangeSet() {
        DatabaseChangeLog changeLog = new DatabaseChangeLog("changelog.xml");
        ChangeSet changeSet = new ChangeSet("id-1", "tester", false, false, "changelog.xml", null, null, changeLog);

        assertThat(ChangeLogLintingException.from(changeLog, changeSet, "Oops")).hasMessage(
            "File name: changelog.xml -- Change Set ID: id-1 -- Author: tester -- Message: Oops"
        );
    }

    @Test
    void shouldDescribeChangeLog() {
        DatabaseChangeLog changeLog = new DatabaseChangeLog("changelog.xml");

        assertThat(ChangeLogLintingException.from(changeLog, null, "Oops")).hasMessage(
            "File name: changelog.xml -- Message: Oops"
        );
    }

    @Test
    void shouldOnlyContainMessageWithoutChangeLogNorChangeSet() {
        assertThat(ChangeLogLintingException.from(null, null, "Oops")).hasMessage("Oops");
    }
}
