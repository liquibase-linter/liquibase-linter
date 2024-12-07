package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.core.AddUniqueConstraintChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UniqueConstraintTablespaceRuleTest {

    private final UniqueConstraintTablespaceRule rule = new UniqueConstraintTablespaceRule();

    @Test
    void shouldHaveName() {
        assertThat(rule.getName()).isEqualTo("unique-constraint-tablespace");
    }

    @Test
    @DisplayName("Tablespace should not be empty")
    void indexTablespaceShouldNotBeEmpty() {
        rule.configure(RuleConfig.EMPTY);

        assertThat(rule.invalid(createUniqueConstraintWithTablespace(null))).isTrue();
        assertThat(rule.getMessage(createUniqueConstraintWithTablespace(null))).isEqualTo("Tablespace '' of unique constraint 'uniq_constraint_foo' is empty or does not follow pattern ''");
    }

    @Test
    @DisplayName("Tablespace must follow pattern")
    void uniqueConstraintTablespaceMustFollowPattern() {
        rule.configure(RuleConfig.builder().withPattern("^TAB_IDX_[A-Z_]+$").build());

        assertThat(rule.invalid(createUniqueConstraintWithTablespace("INDEXES"))).isTrue();
        assertThat(rule.getMessage(createUniqueConstraintWithTablespace("INDEXES"))).isEqualTo("Tablespace 'INDEXES' of unique constraint 'uniq_constraint_foo' is empty or does not follow pattern '^TAB_IDX_[A-Z_]+$'");

        assertThat(rule.invalid(createUniqueConstraintWithTablespace("TAB_IDX_USERS"))).isFalse();
    }

    @DisplayName("Should support formatted error message with pattern arg")
    @Test
    void uniqueConstraintTablespaceRuleShouldReturnFormattedErrorMessage() {
        rule.configure(RuleConfig.builder().withPattern("^TAB_IDX_[A-Z_]+$").withErrorMessage("Tablespace '%s' for unique constraint '%s' must follow pattern '%s'").build());

        assertThat(rule.getMessage(createUniqueConstraintWithTablespace("DDD-001"))).isEqualTo("Tablespace 'DDD-001' for unique constraint 'uniq_constraint_foo' must follow pattern '^TAB_IDX_[A-Z_]+$'");
    }

    private AddUniqueConstraintChange createUniqueConstraintWithTablespace(String tablespace) {
        AddUniqueConstraintChange change = new AddUniqueConstraintChange();
        change.setTablespace(tablespace);
        change.setConstraintName("uniq_constraint_foo");
        return change;
    }
}
