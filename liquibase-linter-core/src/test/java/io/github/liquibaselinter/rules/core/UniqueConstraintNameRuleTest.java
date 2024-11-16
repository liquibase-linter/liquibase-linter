package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.core.UniqueConstraintNameRule;
import liquibase.change.core.AddUniqueConstraintChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UniqueConstraintNameRuleTest {

    private UniqueConstraintNameRule uniqueConstraintNameRule;

    @BeforeEach
    void setUp() {
        uniqueConstraintNameRule = new UniqueConstraintNameRule();
    }

    @DisplayName("Unique constraint name must not be null")
    @Test
    void uniqueConstraintNameNameMustNotBeNull() {
        assertThat(uniqueConstraintNameRule.invalid(getAddUniqueConstraintChange(null))).isTrue();
    }

    @DisplayName("Unique constraint name must follow pattern")
    @Test
    void uniqueConstraintNameNameMustFollowPattern() {
        uniqueConstraintNameRule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").build());

        assertThat(uniqueConstraintNameRule.invalid(getAddUniqueConstraintChange("TBL_INVALID"))).isTrue();

        assertThat(uniqueConstraintNameRule.invalid(getAddUniqueConstraintChange("TABLE_VALID"))).isFalse();
    }

    @DisplayName("Unique constraint name rule should support formatted error message with pattern arg")
    @Test
    void uniqueConstraintNameNameRuleShouldReturnFormattedErrorMessage() {
        uniqueConstraintNameRule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").withErrorMessage("Unique constraint name '%s' must follow pattern '%s'").build());
        assertThat(uniqueConstraintNameRule.getMessage(getAddUniqueConstraintChange("TBL_INVALID"))).isEqualTo("Unique constraint name 'TBL_INVALID' must follow pattern '^(?!TBL)[A-Z_]+(?<!_)$'");
    }

    private AddUniqueConstraintChange getAddUniqueConstraintChange(String constraintName) {
        AddUniqueConstraintChange addUniqueConstraintChange = new AddUniqueConstraintChange();
        addUniqueConstraintChange.setConstraintName(constraintName);
        return addUniqueConstraintChange;
    }

}
