package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.change.core.CreateTableChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrimaryKeyNameRuleTest {

    private PrimaryKeyNameRule primaryKeyNameRule;

    @BeforeEach
    void setUp() {
        primaryKeyNameRule = new PrimaryKeyNameRule();
    }

    @Nested
    class AddPrimaryKey {

        @DisplayName("Primary key name must not be null")
        @Test
        void primaryKeyNameMustNotBeNull() {
            assertTrue(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange(null)));
        }

        @DisplayName("Primary key name must follow pattern basic")
        @Test
        void primaryKeyNameMustFollowPatternBasic() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK$").build());
            assertTrue(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange("INVALID_PK")));
            assertFalse(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange("VALID_PK")));
        }

        @DisplayName("Primary key name must follow pattern dynamic value")
        @Test
        void primaryKeyNameMustFollowPatternDynamicValue() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^{{value}}_PK$").withDynamicValue("tableName").build());
            assertTrue(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange("INVALID_PK")));
            assertFalse(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange("TABLE_PK")));
        }

        @DisplayName("Primary key name rule should support formatted error message with pattern arg")
        @Test
        void primaryKeyNameRuleShouldReturnFormattedErrorMessage() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK$").withErrorMessage("Primary key constraints %s must follow pattern '%s'").build());
            assertEquals(primaryKeyNameRule.getMessage(getAddPrimaryKeyConstraintChange("INVALID_PK")), "Primary key constraints INVALID_PK must follow pattern '^VALID_PK$'");
        }

        private AddPrimaryKeyChange getAddPrimaryKeyConstraintChange(String constraintName) {
            AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
            addPrimaryKeyChange.setConstraintName(constraintName);
            addPrimaryKeyChange.setTableName("TABLE");
            return addPrimaryKeyChange;
        }
    }

    @Nested
    class CreateTable {

        @DisplayName("Primary key name must not be null")
        @Test
        void primaryKeyNameMustNotBeNull() {
            assertTrue(primaryKeyNameRule.invalid(createTableChange(null)));
        }

        @DisplayName("Primary key name must follow pattern basic")
        @Test
        void primaryKeyNameMustFollowPatternBasic() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK$").build());
            assertTrue(primaryKeyNameRule.invalid(createTableChange("INVALID_PK")));
            assertFalse(primaryKeyNameRule.invalid(createTableChange("VALID_PK")));
        }

        @DisplayName("Primary key name must follow pattern dynamic value")
        @Test
        void primaryKeyNameMustFollowPatternDynamicValue() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^{{value}}_PK$").withDynamicValue("tableName").build());
            assertTrue(primaryKeyNameRule.invalid(createTableChange("INVALID_PK")));
            assertFalse(primaryKeyNameRule.invalid(createTableChange("TABLE_PK")));
        }

        @DisplayName("Primary key name rule should support formatted error message with pattern arg")
        @Test
        void primaryKeyNameRuleShouldReturnFormattedErrorMessage() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK$").withErrorMessage("Primary key constraints %s must follow pattern '%s'").build());
            assertEquals(primaryKeyNameRule.getMessage(createTableChange("INVALID_PK")), "Primary key constraints INVALID_PK must follow pattern '^VALID_PK$'");
        }

        private CreateTableChange createTableChange(String primaryKeyName) {
            ConstraintsConfig constraints = new ConstraintsConfig();
            constraints.setPrimaryKeyName(primaryKeyName);
            constraints.setPrimaryKey("true");

            ColumnConfig column = new ColumnConfig();
            column.setConstraints(constraints);

            CreateTableChange createTableChange = new CreateTableChange();
            createTableChange.setTableName("TABLE");
            createTableChange.addColumn(column);
            return createTableChange;
        }
    }
}
