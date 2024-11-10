package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChangetSetIdRuleTest {

    private final ChangetSetIdRule rule = new ChangetSetIdRule();

    @Test
    void shouldHaveName() {
        assertThat(rule.getName()).isEqualTo("changeset-id");
    }

    @Test
    @DisplayName("ChangeSet author must follow pattern")
    void changeSetIdMustFollowPattern() {
        rule.configure(RuleConfig.builder().withPattern("^\\d{8}_[a-z_]+$").build());

        assertThat(rule.invalid(changeSetWithId("create_table_foo"))).isTrue();
        assertThat(rule.getMessage(changeSetWithId("create_table_foo"))).isEqualTo("ChangeSet id 'create_table_foo' does not follow pattern '^\\d{8}_[a-z_]+$'");

        assertThat(rule.invalid(changeSetWithId("20240509_create_table_foo"))).isFalse();
    }

    @DisplayName("Should support formatted error message with pattern arg")
    @Test
    void changeSetIdRuleShouldReturnFormattedErrorMessage() {
        rule.configure(RuleConfig.builder().withPattern("^\\d$").withErrorMessage("The changeset id '%s' must follow pattern '%s'").build());

        assertThat(rule.getMessage(changeSetWithId("DDD-001"))).isEqualTo("The changeset id 'DDD-001' must follow pattern '^\\d$'");
    }

    private ChangeSet changeSetWithId(String id) {
        return new ChangeSet(id, "author", true, true, "filePath", "context", "postgresql", new DatabaseChangeLog());
    }
}
