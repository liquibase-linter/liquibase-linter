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

import static org.assertj.core.api.Assertions.assertThat;

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
            assertThat(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange(null))).isTrue();
        }

        @DisplayName("Primary key name must follow pattern basic")
        @Test
        void primaryKeyNameMustFollowPatternBasic() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK$").build());
            assertThat(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange("INVALID_PK"))).isTrue();
            assertThat(primaryKeyNameRule.getMessage(getAddPrimaryKeyConstraintChange("INVALID_PK"))).isEqualTo("Primary key name 'INVALID_PK' is missing or does not follow pattern '^VALID_PK$'");

            assertThat(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange("VALID_PK"))).isFalse();
        }

        @DisplayName("Primary key name must follow pattern dynamic value")
        @Test
        void primaryKeyNameMustFollowPatternDynamicValue() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^{{value}}_PK$").withDynamicValue("tableName").build());
            assertThat(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange("INVALID_PK"))).isTrue();
            assertThat(primaryKeyNameRule.getMessage(getAddPrimaryKeyConstraintChange("INVALID_PK"))).isEqualTo("Primary key name 'INVALID_PK' is missing or does not follow pattern '^TABLE_PK$'");

            assertThat(primaryKeyNameRule.invalid(getAddPrimaryKeyConstraintChange("TABLE_PK"))).isFalse();
        }

        @DisplayName("Primary key name rule should support formatted error message with pattern arg")
        @Test
        void primaryKeyNameRuleShouldReturnFormattedErrorMessage() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK$").withErrorMessage("Primary key constraints %s must follow pattern '%s'").build());
            assertThat(primaryKeyNameRule.getMessage(getAddPrimaryKeyConstraintChange("INVALID_PK"))).isEqualTo("Primary key constraints INVALID_PK must follow pattern '^VALID_PK$'");
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
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK$").build());

            CreateTableChange change = new CreateTableChange();
            change.setTableName("TABLE");
            change.addColumn(columnWithPrimaryKeyConstraint(null, true));

            assertThat(primaryKeyNameRule.supports(change)).isTrue();
            assertThat(primaryKeyNameRule.invalid(change)).isTrue();
        }

        @DisplayName("Primary key name must follow pattern basic")
        @Test
        void primaryKeyNameMustFollowPatternBasic() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK$").build());

            CreateTableChange invalidChange = createTableChange("INVALID_PK");
            assertThat(primaryKeyNameRule.supports(invalidChange)).isTrue();
            assertThat(primaryKeyNameRule.invalid(invalidChange)).isTrue();

            CreateTableChange validChange = createTableChange("VALID_PK");
            assertThat(primaryKeyNameRule.supports(validChange)).isTrue();
            assertThat(primaryKeyNameRule.invalid(validChange)).isFalse();
        }

        @DisplayName("Primary key name must follow pattern dynamic value")
        @Test
        void primaryKeyNameMustFollowPatternDynamicValue() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^{{value}}_PK$").withDynamicValue("tableName").build());
            assertThat(primaryKeyNameRule.invalid(createTableChange("INVALID_PK"))).isTrue();
            assertThat(primaryKeyNameRule.invalid(createTableChange("TABLE_PK"))).isFalse();
        }

        @DisplayName("Primary key name rule should support formatted error message with pattern arg")
        @Test
        void primaryKeyNameRuleShouldReturnFormattedErrorMessage() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK$").withErrorMessage("Primary key constraints %s must follow pattern '%s'").build());
            assertThat(primaryKeyNameRule.getMessage(createTableChange("INVALID_PK"))).isEqualTo("Primary key constraints INVALID_PK must follow pattern '^VALID_PK$'");
        }

        @Test
        @DisplayName("Name of composite primary key should only be reported once")
        void compositePrimaryKeyNameShouldOnlyBeReportedOnce() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK$").withErrorMessage("Primary key constraints %s must follow pattern '%s'").build());

            CreateTableChange createTableChange = new CreateTableChange();
            createTableChange.setTableName("TABLE");
            createTableChange.addColumn(columnWithPrimaryKeyConstraint("INVALID_PK", false));
            createTableChange.addColumn(columnWithPrimaryKeyConstraint("INVALID_PK", false));

            assertThat(primaryKeyNameRule.getMessage(createTableChange)).isEqualTo("Primary key constraints INVALID_PK must follow pattern '^VALID_PK$'");
        }

        @Test
        @DisplayName("Creating a table without primary key should not be invalid")
        void createTableWithoutPrimaryKeyShouldNotBeInvalid() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK$").build());

            CreateTableChange createTableChange = new CreateTableChange();
            createTableChange.setTableName("TABLE");
            createTableChange.addColumn(new ColumnConfig());

            assertThat(primaryKeyNameRule.supports(createTableChange)).isFalse();
        }

        @Test
        @DisplayName("A table with a valid primary key and an invalid primary key should only report invalid primary key")
        void createTableWithMultiplePrimaryKeysShouldDetectInvalidPrimaryKey() {
            primaryKeyNameRule.configure(RuleConfig.builder().withPattern("^VALID_PK.*$").build());

            CreateTableChange createTableChange = new CreateTableChange();
            createTableChange.setTableName("TABLE");
            createTableChange.addColumn(columnWithPrimaryKeyConstraint("VALID_PK", false));
            createTableChange.addColumn(columnWithPrimaryKeyConstraint("INVALID_PK", true));

            assertThat(primaryKeyNameRule.invalid(createTableChange)).isTrue();
            assertThat(primaryKeyNameRule.getMessage(createTableChange)).isEqualTo("Primary key name 'INVALID_PK' is missing or does not follow pattern '^VALID_PK.*$'");
        }

        private CreateTableChange createTableChange(String primaryKeyName) {

            CreateTableChange createTableChange = new CreateTableChange();
            createTableChange.setTableName("TABLE");
            createTableChange.addColumn(columnWithPrimaryKeyConstraint(primaryKeyName, false));
            return createTableChange;
        }

        private ColumnConfig columnWithPrimaryKeyConstraint(String primaryKeyName, Boolean isPrimaryKey) {
            ConstraintsConfig constraints = new ConstraintsConfig();
            constraints.setPrimaryKeyName(primaryKeyName);
            constraints.setPrimaryKey(isPrimaryKey);

            ColumnConfig column = new ColumnConfig();
            column.setConstraints(constraints);
            return column;
        }
    }
}
