package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.core.AddUniqueConstraintChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UniqueConstraintNameRuleTest {

    private final UniqueConstraintNameRule rule = new UniqueConstraintNameRule();

    @DisplayName("Unique constraint name must not be null")
    @Test
    void uniqueConstraintNameNameMustNotBeNull() {
        assertThat(rule.invalid(getAddUniqueConstraintChange(null))).isTrue();
    }

    @DisplayName("Unique constraint name must follow pattern")
    @Test
    void uniqueConstraintNameNameMustFollowPattern() {
        rule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").build());

        assertThat(rule.invalid(getAddUniqueConstraintChange("TBL_INVALID"))).isTrue();

        assertThat(rule.invalid(getAddUniqueConstraintChange("TABLE_VALID"))).isFalse();
    }

    @DisplayName("Unique constraint name rule should support formatted error message with pattern arg")
    @Test
    void uniqueConstraintNameNameRuleShouldReturnFormattedErrorMessage() {
        rule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").withErrorMessage("Unique constraint name '%s' must follow pattern '%s'").build());
        assertThat(rule.getMessage(getAddUniqueConstraintChange("TBL_INVALID"))).isEqualTo("Unique constraint name 'TBL_INVALID' must follow pattern '^(?!TBL)[A-Z_]+(?<!_)$'");
    }

    private AddUniqueConstraintChange getAddUniqueConstraintChange(String constraintName) {
        AddUniqueConstraintChange addUniqueConstraintChange = new AddUniqueConstraintChange();
        addUniqueConstraintChange.setConstraintName(constraintName);
        return addUniqueConstraintChange;
    }

}
