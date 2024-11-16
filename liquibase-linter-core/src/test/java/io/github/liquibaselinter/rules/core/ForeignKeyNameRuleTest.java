package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.core.AddForeignKeyConstraintChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ForeignKeyNameRuleTest {

    private final ForeignKeyNameRule rule = new ForeignKeyNameRule();

    @DisplayName("Foreign key name must not be null")
    @Test
    void foreignKeyNameMustNotBeNull() {
        assertThat(rule.invalid(getAddForeignKeyConstraintChange(null))).isTrue();
    }

    @DisplayName("Foreign key name must follow pattern basic")
    @Test
    void foreignKeyNameMustFollowPatternBasic() {
        rule.configure(RuleConfig.builder().withPattern("^VALID_FK$").build());
        assertThat(rule.invalid(getAddForeignKeyConstraintChange("INVALID_FK"))).isTrue();
        assertThat(rule.invalid(getAddForeignKeyConstraintChange("VALID_FK"))).isFalse();
    }

    @DisplayName("Foreign key name must follow pattern dynamic value")
    @Test
    void foreignKeyNameMustFollowPatternDynamicValue() {
        rule.configure(RuleConfig.builder().withPattern("^{{value}}_FK$").withDynamicValue("baseTableName + '_' + referencedTableName").build());
        assertThat(rule.invalid(getAddForeignKeyConstraintChange("INVALID_FK"))).isTrue();
        assertThat(rule.invalid(getAddForeignKeyConstraintChange("BASE_REFERENCED_FK"))).isFalse();
    }

    @DisplayName("Foreign key name rule should support formatted error message with pattern arg")
    @Test
    void foreignKeyNameRuleShouldReturnFormattedErrorMessage() {
        rule.configure(RuleConfig.builder().withPattern("^VALID_FK$").withErrorMessage("Foreign key constraint '%s' must follow pattern '%s'").build());
        assertThat(rule.getMessage(getAddForeignKeyConstraintChange("INVALID_FK"))).isEqualTo("Foreign key constraint 'INVALID_FK' must follow pattern '^VALID_FK$'");
    }

    private AddForeignKeyConstraintChange getAddForeignKeyConstraintChange(String constraintName) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setConstraintName(constraintName);
        addForeignKeyConstraintChange.setBaseTableName("BASE");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCED");
        return addForeignKeyConstraintChange;
    }
}
