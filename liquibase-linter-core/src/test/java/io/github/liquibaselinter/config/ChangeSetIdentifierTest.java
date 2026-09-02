package io.github.liquibaselinter.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import org.junit.jupiter.api.Test;

class ChangeSetIdentifierTest {

    private static ChangeSet changeSet(String id, String author, String filePath) {
        return new ChangeSet(id, author, true, true, filePath, "context", "postgresql", new DatabaseChangeLog());
    }

    @Test
    void shouldRejectMissingId() {
        assertThatThrownBy(() -> new ChangeSetIdentifier("changelog.xml", null, "dba"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("'changeLogFile', 'id' and 'author'");
    }

    @Test
    void shouldRejectMissingAuthor() {
        assertThatThrownBy(() -> new ChangeSetIdentifier("changelog.xml", "create-table", "  ")).isInstanceOf(
            IllegalArgumentException.class
        );
    }

    @Test
    void shouldRejectMissingChangeLogFile() {
        assertThatThrownBy(() -> new ChangeSetIdentifier("", "create-table", "dba")).isInstanceOf(
            IllegalArgumentException.class
        );
    }

    @Test
    void shouldMatchOnTheFullIdentity() {
        ChangeSetIdentifier identifier = new ChangeSetIdentifier("db/2024/init.xml", "create-table", "dba");

        assertThat(identifier.matches(changeSet("create-table", "dba", "db/2024/init.xml"))).isTrue();
    }

    @Test
    void shouldNotMatchWhenAnyAttributeDiffers() {
        ChangeSetIdentifier identifier = new ChangeSetIdentifier("db/2024/init.xml", "create-table", "dba");

        assertThat(identifier.matches(changeSet("other", "dba", "db/2024/init.xml"))).isFalse();
        assertThat(identifier.matches(changeSet("create-table", "someone-else", "db/2024/init.xml"))).isFalse();
        assertThat(identifier.matches(changeSet("create-table", "dba", "db/2025/init.xml"))).isFalse();
    }

    @Test
    void shouldExposeAttributes() {
        ChangeSetIdentifier identifier = new ChangeSetIdentifier("db/2024/init.xml", "create-table", "dba");

        assertThat(identifier.getChangeLogFile()).isEqualTo("db/2024/init.xml");
        assertThat(identifier.getId()).isEqualTo("create-table");
        assertThat(identifier.getAuthor()).isEqualTo("dba");
    }

    @Test
    void shouldRenderReadableToString() {
        assertThat(new ChangeSetIdentifier("db/init.xml", "create-table", "dba")).hasToString(
            "db/init.xml::create-table::dba"
        );
    }
}
