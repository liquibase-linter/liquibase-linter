package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.core.AddUniqueConstraintChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UniqueConstraintNameRuleTest {

    private final UniqueConstraintNameRule rule = new UniqueConstraintNameRule();

    @DisplayName("Unique constraint name must not be null")
    @Test
    void uniqueConstraintNameNameMustNotBeNull() {
        assertThat(rule)
            .checkingChange(getAddUniqueConstraintChange(null))
            .hasExactlyViolationsMessages("Unique constraint name does not follow pattern");
    }

    @DisplayName("Unique constraint name must follow pattern")
    @Test
    void uniqueConstraintNameNameMustFollowPattern() {
        RuleConfig ruleConfig = RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddUniqueConstraintChange("TBL_INVALID"))
            .hasViolations();
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddUniqueConstraintChange("TABLE_VALID"))
            .hasNoViolations();
    }

    @DisplayName("Unique constraint name rule should support formatted error message with pattern arg")
    @Test
    void uniqueConstraintNameNameRuleShouldReturnFormattedErrorMessage() {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withPattern("^(?!TBL)[A-Z_]+(?<!_)$")
            .withErrorMessage("Unique constraint name '%s' must follow pattern '%s'")
            .build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddUniqueConstraintChange("TBL_INVALID"))
            .hasExactlyViolationsMessages(
                "Unique constraint name 'TBL_INVALID' must follow pattern '^(?!TBL)[A-Z_]+(?<!_)$'"
            );
    }

    private AddUniqueConstraintChange getAddUniqueConstraintChange(String constraintName) {
        AddUniqueConstraintChange addUniqueConstraintChange = new AddUniqueConstraintChange();
        addUniqueConstraintChange.setConstraintName(constraintName);
        return addUniqueConstraintChange;
    }
}
