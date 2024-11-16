package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.core.ForeignKeyNameRule;
import liquibase.change.core.AddForeignKeyConstraintChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ForeignKeyNameRuleTest {

    private ForeignKeyNameRule foreignKeyNameRule;

    @BeforeEach
    void setUp() {
        foreignKeyNameRule = new ForeignKeyNameRule();
    }

    @DisplayName("Foreign key name must not be null")
    @Test
    void foreignKeyNameMustNotBeNull() {
        assertThat(foreignKeyNameRule.invalid(getAddForeignKeyConstraintChange(null))).isTrue();
    }

    @DisplayName("Foreign key name must follow pattern basic")
    @Test
    void foreignKeyNameMustFollowPatternBasic() {
        foreignKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_FK$").build());
        assertThat(foreignKeyNameRule.invalid(getAddForeignKeyConstraintChange("INVALID_FK"))).isTrue();
        assertThat(foreignKeyNameRule.invalid(getAddForeignKeyConstraintChange("VALID_FK"))).isFalse();
    }

    @DisplayName("Foreign key name must follow pattern dynamic value")
    @Test
    void foreignKeyNameMustFollowPatternDynamicValue() {
        foreignKeyNameRule.configure(RuleConfig.builder().withPattern("^{{value}}_FK$").withDynamicValue("baseTableName + '_' + referencedTableName").build());
        assertThat(foreignKeyNameRule.invalid(getAddForeignKeyConstraintChange("INVALID_FK"))).isTrue();
        assertThat(foreignKeyNameRule.invalid(getAddForeignKeyConstraintChange("BASE_REFERENCED_FK"))).isFalse();
    }

    @DisplayName("Foreign key name rule should support formatted error message with pattern arg")
    @Test
    void foreignKeyNameRuleShouldReturnFormattedErrorMessage() {
        foreignKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_FK$").withErrorMessage("Foreign key constraint '%s' must follow pattern '%s'").build());
        assertThat(foreignKeyNameRule.getMessage(getAddForeignKeyConstraintChange("INVALID_FK"))).isEqualTo("Foreign key constraint 'INVALID_FK' must follow pattern '^VALID_FK$'");
    }

    private AddForeignKeyConstraintChange getAddForeignKeyConstraintChange(String constraintName) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setConstraintName(constraintName);
        addForeignKeyConstraintChange.setBaseTableName("BASE");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCED");
        return addForeignKeyConstraintChange;
    }
}
