package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChangetSetAuthorRuleTest {

    private final ChangetSetAuthorRule rule = new ChangetSetAuthorRule();

    @Test
    void shouldHaveName() {
        assertThat(rule.getName()).isEqualTo("changeset-author");
    }

    @Test
    @DisplayName("ChangeSet author must follow pattern")
    void changeSetAuthorMustFollowPattern() {
        rule.configure(RuleConfig.builder().withPattern("^John$").build());

        assertThat(rule.invalid(changeSetWithAuthor("Jane"))).isTrue();
        assertThat(rule.getMessage(changeSetWithAuthor("Jane"))).isEqualTo("ChangeSet author 'Jane' does not follow pattern '^John$'");

        assertThat(rule.invalid(changeSetWithAuthor("John"))).isFalse();
    }

    @DisplayName("Should support formatted error message with pattern arg")
    @Test
    void changeSetAuthorRuleShouldReturnFormattedErrorMessage() {
        rule.configure(RuleConfig.builder().withPattern("^John$").withErrorMessage("The author '%s' must follow pattern '%s'").build());

        assertThat(rule.getMessage(changeSetWithAuthor("Jane"))).isEqualTo("The author 'Jane' must follow pattern '^John$'");
    }

    private ChangeSet changeSetWithAuthor(String author) {
        return new ChangeSet("id", author, true, true, "filePath", "context", "postgresql", new DatabaseChangeLog());
    }
}
