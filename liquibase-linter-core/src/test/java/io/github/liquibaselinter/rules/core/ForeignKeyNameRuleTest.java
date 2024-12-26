package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.core.AddForeignKeyConstraintChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ForeignKeyNameRuleTest {

    private final ForeignKeyNameRule rule = new ForeignKeyNameRule();

    @DisplayName("Foreign key name must not be null")
    @Test
    void foreignKeyNameMustNotBeNull() {
        assertThat(rule)
            .checkingChange(getAddForeignKeyConstraintChange(null))
            .hasExactlyViolationsMessages("Foreign key name is missing or does not follow pattern");
    }

    @DisplayName("Foreign key name must follow pattern basic")
    @Test
    void foreignKeyNameMustFollowPatternBasic() {
        RuleConfig ruleConfig = RuleConfig.builder().withPattern("^VALID_FK$").build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddForeignKeyConstraintChange("INVALID_FK"))
            .hasExactlyViolationsMessages("Foreign key name is missing or does not follow pattern");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddForeignKeyConstraintChange("VALID_FK"))
            .hasNoViolations();
    }

    @DisplayName("Foreign key name must follow pattern dynamic value")
    @Test
    void foreignKeyNameMustFollowPatternDynamicValue() {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withPattern("^{{value}}_FK$")
            .withDynamicValue("baseTableName + '_' + referencedTableName")
            .build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddForeignKeyConstraintChange("INVALID_FK"))
            .hasExactlyViolationsMessages("Foreign key name is missing or does not follow pattern");

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddForeignKeyConstraintChange("BASE_REFERENCED_FK"))
            .hasNoViolations();
    }

    @DisplayName("Foreign key name rule should support formatted error message with pattern arg")
    @Test
    void foreignKeyNameRuleShouldReturnFormattedErrorMessage() {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withPattern("^VALID_FK$")
            .withErrorMessage("Foreign key constraint '%s' must follow pattern '%s'")
            .build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddForeignKeyConstraintChange("INVALID_FK"))
            .hasExactlyViolationsMessages("Foreign key constraint 'INVALID_FK' must follow pattern '^VALID_FK$'");
    }

    private AddForeignKeyConstraintChange getAddForeignKeyConstraintChange(String constraintName) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setConstraintName(constraintName);
        addForeignKeyConstraintChange.setBaseTableName("BASE");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCED");
        return addForeignKeyConstraintChange;
    }
}
