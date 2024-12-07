package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.change.core.CreateTableChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class PrimaryKeyNameRuleTest {

    private final PrimaryKeyNameRule rule = new PrimaryKeyNameRule();

    @Nested
    class AddPrimaryKey {

        @DisplayName("Primary key name must not be null")
        @Test
        void primaryKeyNameMustNotBeNull() {
            assertThat(rule.invalid(getAddPrimaryKeyConstraintChange(null))).isTrue();
        }

        @DisplayName("Primary key name must follow pattern basic")
        @Test
        void primaryKeyNameMustFollowPatternBasic() {
            rule.configure(RuleConfig.builder().withPattern("^VALID_PK$").build());
            assertThat(rule.invalid(getAddPrimaryKeyConstraintChange("INVALID_PK"))).isTrue();
            assertThat(rule.getMessage(getAddPrimaryKeyConstraintChange("INVALID_PK"))).isEqualTo(
                "Primary key name 'INVALID_PK' is missing or does not follow pattern '^VALID_PK$'"
            );

            assertThat(rule.invalid(getAddPrimaryKeyConstraintChange("VALID_PK"))).isFalse();
        }

        @DisplayName("Primary key name must follow pattern dynamic value")
        @Test
        void primaryKeyNameMustFollowPatternDynamicValue() {
            rule.configure(RuleConfig.builder().withPattern("^{{value}}_PK$").withDynamicValue("tableName").build());
            assertThat(rule.invalid(getAddPrimaryKeyConstraintChange("INVALID_PK"))).isTrue();
            assertThat(rule.getMessage(getAddPrimaryKeyConstraintChange("INVALID_PK"))).isEqualTo(
                "Primary key name 'INVALID_PK' is missing or does not follow pattern '^TABLE_PK$'"
            );

            assertThat(rule.invalid(getAddPrimaryKeyConstraintChange("TABLE_PK"))).isFalse();
        }

        @DisplayName("Primary key name rule should support formatted error message with pattern arg")
        @Test
        void primaryKeyNameRuleShouldReturnFormattedErrorMessage() {
            rule.configure(
                RuleConfig.builder()
                    .withPattern("^VALID_PK$")
                    .withErrorMessage("Primary key constraints %s must follow pattern '%s'")
                    .build()
            );
            assertThat(rule.getMessage(getAddPrimaryKeyConstraintChange("INVALID_PK"))).isEqualTo(
                "Primary key constraints INVALID_PK must follow pattern '^VALID_PK$'"
            );
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
            rule.configure(RuleConfig.builder().withPattern("^VALID_PK$").build());

            CreateTableChange change = new CreateTableChange();
            change.setTableName("TABLE");
            change.addColumn(columnWithPrimaryKeyConstraint(null, true));

            assertThat(rule.supports(change)).isTrue();
            assertThat(rule.invalid(change)).isTrue();
        }

        @DisplayName("Primary key name must follow pattern basic")
        @Test
        void primaryKeyNameMustFollowPatternBasic() {
            rule.configure(RuleConfig.builder().withPattern("^VALID_PK$").build());

            CreateTableChange invalidChange = createTableChange("INVALID_PK");
            assertThat(rule.supports(invalidChange)).isTrue();
            assertThat(rule.invalid(invalidChange)).isTrue();

            CreateTableChange validChange = createTableChange("VALID_PK");
            assertThat(rule.supports(validChange)).isTrue();
            assertThat(rule.invalid(validChange)).isFalse();
        }

        @DisplayName("Primary key name must follow pattern dynamic value")
        @Test
        void primaryKeyNameMustFollowPatternDynamicValue() {
            rule.configure(RuleConfig.builder().withPattern("^{{value}}_PK$").withDynamicValue("tableName").build());
            assertThat(rule.invalid(createTableChange("INVALID_PK"))).isTrue();
            assertThat(rule.invalid(createTableChange("TABLE_PK"))).isFalse();
        }

        @DisplayName("Primary key name rule should support formatted error message with pattern arg")
        @Test
        void primaryKeyNameRuleShouldReturnFormattedErrorMessage() {
            rule.configure(
                RuleConfig.builder()
                    .withPattern("^VALID_PK$")
                    .withErrorMessage("Primary key constraints %s must follow pattern '%s'")
                    .build()
            );
            assertThat(rule.getMessage(createTableChange("INVALID_PK"))).isEqualTo(
                "Primary key constraints INVALID_PK must follow pattern '^VALID_PK$'"
            );
        }

        @Test
        @DisplayName("Name of composite primary key should only be reported once")
        void compositePrimaryKeyNameShouldOnlyBeReportedOnce() {
            rule.configure(
                RuleConfig.builder()
                    .withPattern("^VALID_PK$")
                    .withErrorMessage("Primary key constraints %s must follow pattern '%s'")
                    .build()
            );

            CreateTableChange createTableChange = new CreateTableChange();
            createTableChange.setTableName("TABLE");
            createTableChange.addColumn(columnWithPrimaryKeyConstraint("INVALID_PK", false));
            createTableChange.addColumn(columnWithPrimaryKeyConstraint("INVALID_PK", false));

            assertThat(rule.getMessage(createTableChange)).isEqualTo(
                "Primary key constraints INVALID_PK must follow pattern '^VALID_PK$'"
            );
        }

        @Test
        @DisplayName("Creating a table without primary key should not be invalid")
        void createTableWithoutPrimaryKeyShouldNotBeInvalid() {
            rule.configure(RuleConfig.builder().withPattern("^VALID_PK$").build());

            CreateTableChange createTableChange = new CreateTableChange();
            createTableChange.setTableName("TABLE");
            createTableChange.addColumn(new ColumnConfig());

            assertThat(rule.supports(createTableChange)).isFalse();
        }

        @Test
        @DisplayName(
            "A table with a valid primary key and an invalid primary key should only report invalid primary key"
        )
        void createTableWithMultiplePrimaryKeysShouldDetectInvalidPrimaryKey() {
            rule.configure(RuleConfig.builder().withPattern("^VALID_PK.*$").build());

            CreateTableChange createTableChange = new CreateTableChange();
            createTableChange.setTableName("TABLE");
            createTableChange.addColumn(columnWithPrimaryKeyConstraint("VALID_PK", false));
            createTableChange.addColumn(columnWithPrimaryKeyConstraint("INVALID_PK", true));

            assertThat(rule.invalid(createTableChange)).isTrue();
            assertThat(rule.getMessage(createTableChange)).isEqualTo(
                "Primary key name 'INVALID_PK' is missing or does not follow pattern '^VALID_PK.*$'"
            );
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
