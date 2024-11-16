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

class PrimaryKeyTablespaceRuleTest {

    private final PrimaryKeyTablespaceRule primaryKeyTablespaceRule = new PrimaryKeyTablespaceRule();

    @Nested
    class AddPrimaryKey {

        @DisplayName("Primary key tablespace must not be null")
        @Test
        void primaryKeyNameMustNotBeNull() {
            assertThat(primaryKeyTablespaceRule.invalid(addPrimaryKeyWithTablespace(null))).isTrue();
        }

        @DisplayName("Primary key tablespace must follow pattern basic")
        @Test
        void primaryKeyNameMustFollowPatternBasic() {
            primaryKeyTablespaceRule.configure(RuleConfig.builder().withPattern("^VALID_TABLESPACE$").build());

            assertThat(primaryKeyTablespaceRule.invalid(addPrimaryKeyWithTablespace("INVALID_TABLESPACE"))).isTrue();
            assertThat(primaryKeyTablespaceRule.getMessage(addPrimaryKeyWithTablespace("INVALID_TABLESPACE"))).isEqualTo("Tablespace 'INVALID_TABLESPACE' is empty or does not follow pattern '^VALID_TABLESPACE$'");

            assertThat(primaryKeyTablespaceRule.invalid(addPrimaryKeyWithTablespace("VALID_TABLESPACE"))).isFalse();
        }

        @DisplayName("Primary key tablespace must follow pattern dynamic value")
        @Test
        void primaryKeyNameMustFollowPatternDynamicValue() {
            primaryKeyTablespaceRule.configure(RuleConfig.builder().withPattern("^{{value}}_PK$").withDynamicValue("tableName").build());
            assertThat(primaryKeyTablespaceRule.invalid(addPrimaryKeyWithTablespace("INVALID_TABLESPACE"))).isTrue();
            assertThat(primaryKeyTablespaceRule.getMessage(addPrimaryKeyWithTablespace("INVALID_TABLESPACE"))).isEqualTo("Tablespace 'INVALID_TABLESPACE' is empty or does not follow pattern '^TABLE_PK$'");

            assertThat(primaryKeyTablespaceRule.invalid(addPrimaryKeyWithTablespace("TABLE_PK"))).isFalse();
        }

        @DisplayName("Primary key tablespace rule should support formatted error message with pattern arg")
        @Test
        void primaryKeyNameRuleShouldReturnFormattedErrorMessage() {
            primaryKeyTablespaceRule.configure(RuleConfig.builder().withPattern("^VALID_TABLESPACE$").withErrorMessage("Primary key constraints %s must follow pattern '%s'").build());
            assertThat(primaryKeyTablespaceRule.getMessage(addPrimaryKeyWithTablespace("INVALID_TABLESPACE"))).isEqualTo("Primary key constraints INVALID_TABLESPACE must follow pattern '^VALID_TABLESPACE$'");
        }

        private AddPrimaryKeyChange addPrimaryKeyWithTablespace(String tablespace) {
            AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
            addPrimaryKeyChange.setTablespace(tablespace);
            addPrimaryKeyChange.setConstraintName("PK_TABLE");
            addPrimaryKeyChange.setTableName("TABLE");
            return addPrimaryKeyChange;
        }
    }

    @Nested
    class CreateTable {

        @DisplayName("Primary key tablespace must not be null")
        @Test
        void primaryKeyNameMustNotBeNull() {
            primaryKeyTablespaceRule.configure(RuleConfig.builder().withPattern("^VALID_TABLESPACE$").build());

            CreateTableChange change = new CreateTableChange();
            change.setTableName("TABLE");
            change.addColumn(columnWithPrimaryKeyTablespace(null, true));

            assertThat(primaryKeyTablespaceRule.supports(change)).isTrue();
            assertThat(primaryKeyTablespaceRule.invalid(change)).isTrue();
        }

        @DisplayName("Primary key tablespace must follow pattern basic")
        @Test
        void primaryKeyNameMustFollowPatternBasic() {
            primaryKeyTablespaceRule.configure(RuleConfig.builder().withPattern("^VALID_TABLESPACE$").build());

            CreateTableChange invalidChange = createTableChange("INVALID_TABLESPACE");
            assertThat(primaryKeyTablespaceRule.supports(invalidChange)).isTrue();
            assertThat(primaryKeyTablespaceRule.invalid(invalidChange)).isTrue();

            CreateTableChange validChange = createTableChange("VALID_TABLESPACE");
            assertThat(primaryKeyTablespaceRule.supports(validChange)).isTrue();
            assertThat(primaryKeyTablespaceRule.invalid(validChange)).isFalse();
        }

        @DisplayName("Primary key tablespace must follow pattern dynamic value")
        @Test
        void primaryKeyNameMustFollowPatternDynamicValue() {
            primaryKeyTablespaceRule.configure(RuleConfig.builder().withPattern("^{{value}}_PK$").withDynamicValue("tableName").build());
            assertThat(primaryKeyTablespaceRule.invalid(createTableChange("INVALID_TABLESPACE"))).isTrue();
            assertThat(primaryKeyTablespaceRule.invalid(createTableChange("TABLE_PK"))).isFalse();
        }

        @DisplayName("Primary key tablespace rule should support formatted error message with pattern arg")
        @Test
        void primaryKeyNameRuleShouldReturnFormattedErrorMessage() {
            primaryKeyTablespaceRule.configure(RuleConfig.builder().withPattern("^VALID_TABLESPACE$").withErrorMessage("Primary key constraints %s must follow pattern '%s'").build());
            assertThat(primaryKeyTablespaceRule.getMessage(createTableChange("INVALID_TABLESPACE"))).isEqualTo("Primary key constraints INVALID_TABLESPACE must follow pattern '^VALID_TABLESPACE$'");
        }

        @Test
        @DisplayName("Name of composite primary key should only be reported once")
        void compositePrimaryKeyNameShouldOnlyBeReportedOnce() {
            primaryKeyTablespaceRule.configure(RuleConfig.builder().withPattern("^VALID_TABLESPACE$").withErrorMessage("Primary key constraints %s must follow pattern '%s'").build());

            CreateTableChange createTableChange = new CreateTableChange();
            createTableChange.setTableName("TABLE");
            createTableChange.addColumn(columnWithPrimaryKeyTablespace("INVALID_TABLESPACE", false));
            createTableChange.addColumn(columnWithPrimaryKeyTablespace("INVALID_TABLESPACE", false));

            assertThat(primaryKeyTablespaceRule.getMessage(createTableChange)).isEqualTo("Primary key constraints INVALID_TABLESPACE must follow pattern '^VALID_TABLESPACE$'");
        }

        @Test
        @DisplayName("Creating a table without primary key should not be invalid")
        void createTableWithoutPrimaryKeyShouldNotBeInvalid() {
            primaryKeyTablespaceRule.configure(RuleConfig.builder().withPattern("^VALID_TABLESPACE$").build());

            CreateTableChange createTableChange = new CreateTableChange();
            createTableChange.setTableName("TABLE");
            createTableChange.addColumn(new ColumnConfig());

            assertThat(primaryKeyTablespaceRule.supports(createTableChange)).isFalse();
        }

        @Test
        @DisplayName("A table with a valid primary key and an invalid primary key should only report invalid primary key")
        void createTableWithMultiplePrimaryKeysShouldDetectInvalidPrimaryKey() {
            primaryKeyTablespaceRule.configure(RuleConfig.builder().withPattern("^VALID_TABLESPACE$.*$").build());

            CreateTableChange createTableChange = new CreateTableChange();
            createTableChange.setTableName("TABLE");
            createTableChange.addColumn(columnWithPrimaryKeyTablespace("VALID_TABLESPACE", false));
            createTableChange.addColumn(columnWithPrimaryKeyTablespace("INVALID_TABLESPACE", true));

            assertThat(primaryKeyTablespaceRule.invalid(createTableChange)).isTrue();
            assertThat(primaryKeyTablespaceRule.getMessage(createTableChange)).isEqualTo("Tablespace 'INVALID_TABLESPACE' is empty or does not follow pattern '^VALID_TABLESPACE$.*$'");
        }

        private CreateTableChange createTableChange(String primaryKeyName) {

            CreateTableChange createTableChange = new CreateTableChange();
            createTableChange.setTableName("TABLE");
            createTableChange.addColumn(columnWithPrimaryKeyTablespace(primaryKeyName, false));
            return createTableChange;
        }

        private ColumnConfig columnWithPrimaryKeyTablespace(String tablespace, Boolean isPrimaryKey) {
            ConstraintsConfig constraints = new ConstraintsConfig();
            constraints.setPrimaryKeyTablespace(tablespace);
            constraints.setPrimaryKeyName("pk_table");
            constraints.setPrimaryKey(isPrimaryKey);

            ColumnConfig column = new ColumnConfig();
            column.setConstraints(constraints);
            return column;
        }
    }
}
