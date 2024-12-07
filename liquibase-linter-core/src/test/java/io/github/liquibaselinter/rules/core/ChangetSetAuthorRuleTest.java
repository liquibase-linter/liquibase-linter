package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.RuleViolation;
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
        RuleConfig ruleConfig = RuleConfig.builder().withPattern("^John$").build();

        assertThat(rule.check(changeSetWithAuthor("Jane"), ruleConfig))
            .extracting(RuleViolation::message)
            .containsExactly("ChangeSet author 'Jane' does not follow pattern '^John$'");

        assertThat(rule.check(changeSetWithAuthor("John"), ruleConfig)).isEmpty();
    }

    @DisplayName("Should support formatted error message with pattern arg")
    @Test
    void changeSetAuthorRuleShouldReturnFormattedErrorMessage() {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withPattern("^John$")
            .withErrorMessage("The author '%s' must follow pattern '%s'")
            .build();

        assertThat(rule.check(changeSetWithAuthor("Jane"), ruleConfig))
            .extracting(RuleViolation::message)
            .containsExactly("The author 'Jane' must follow pattern '^John$'");
    }

    private ChangeSet changeSetWithAuthor(String author) {
        return new ChangeSet("id", author, true, true, "filePath", "context", "postgresql", new DatabaseChangeLog());
    }
}
