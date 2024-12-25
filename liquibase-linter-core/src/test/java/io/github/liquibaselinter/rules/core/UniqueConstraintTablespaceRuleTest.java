package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.core.AddUniqueConstraintChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UniqueConstraintTablespaceRuleTest {

    private final UniqueConstraintTablespaceRule rule = new UniqueConstraintTablespaceRule();

    @Test
    void shouldHaveName() {
        assertThat(rule).hasName("unique-constraint-tablespace");
    }

    @Test
    @DisplayName("Tablespace should not be empty")
    void indexTablespaceShouldNotBeEmpty() {
        assertThat(rule)
            .checkingChange(createUniqueConstraintWithTablespace(null))
            .hasExactlyViolationsMessages(
                "Tablespace '' of unique constraint 'uniq_constraint_foo' is empty or does not follow pattern ''"
            );
    }

    @Test
    @DisplayName("Tablespace must follow pattern")
    void uniqueConstraintTablespaceMustFollowPattern() {
        RuleConfig ruleConfig = RuleConfig.builder().withPattern("^TAB_IDX_[A-Z_]+$").build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(createUniqueConstraintWithTablespace("INDEXES"))
            .hasExactlyViolationsMessages(
                "Tablespace 'INDEXES' of unique constraint 'uniq_constraint_foo' is empty or does not follow pattern '^TAB_IDX_[A-Z_]+$'"
            );

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(createUniqueConstraintWithTablespace("TAB_IDX_USERS"))
            .hasNoViolations();
    }

    @DisplayName("Should support formatted error message with pattern arg")
    @Test
    void uniqueConstraintTablespaceRuleShouldReturnFormattedErrorMessage() {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withPattern("^TAB_IDX_[A-Z_]+$")
            .withErrorMessage("Tablespace '%s' for unique constraint '%s' must follow pattern '%s'")
            .build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(createUniqueConstraintWithTablespace("DDD-001"))
            .hasExactlyViolationsMessages(
                "Tablespace 'DDD-001' for unique constraint 'uniq_constraint_foo' must follow pattern '^TAB_IDX_[A-Z_]+$'"
            );
    }

    private AddUniqueConstraintChange createUniqueConstraintWithTablespace(String tablespace) {
        AddUniqueConstraintChange change = new AddUniqueConstraintChange();
        change.setTablespace(tablespace);
        change.setConstraintName("uniq_constraint_foo");
        return change;
    }
}
