package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.core.ObjectNameRules.ObjectNameRule;
import liquibase.change.AddColumnConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.AddForeignKeyConstraintChange;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.change.core.AddUniqueConstraintChange;
import liquibase.change.core.CreateIndexChange;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.CreateViewChange;
import liquibase.change.core.MergeColumnChange;
import liquibase.change.core.RenameColumnChange;
import liquibase.change.core.RenameViewChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ObjectNameRuleTest {

    private final ObjectNameRule rule = new ObjectNameRule();

    @DisplayName("Object name must not be null")
    @Test
    void objectNameMustNotBeNull() {
        RuleConfig ruleConfig = RuleConfig.builder().withPattern("^(?!_)[A-Z_0-9]+(?<!_)$").build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddColumnChange(new String[] { null }))
            .hasExactlyViolationsMessages("Object name does not follow pattern");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddForeignKeyConstraintChange(null))
            .hasExactlyViolationsMessages("Object name does not follow pattern");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddPrimaryKeyConstraintChange(null))
            .hasExactlyViolationsMessages("Object name does not follow pattern");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddUniqueConstraintChange(null))
            .hasExactlyViolationsMessages("Object name does not follow pattern");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getCreateTableChange(null))
            .hasExactlyViolationsMessages("Object name does not follow pattern");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getMergeColumnChange(null))
            .hasExactlyViolationsMessages("Object name does not follow pattern");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getRenameColumnChange(null))
            .hasExactlyViolationsMessages("Object name does not follow pattern");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getRenameViewChange(null))
            .hasExactlyViolationsMessages("Object name does not follow pattern");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getCreateViewChange(null))
            .hasExactlyViolationsMessages("Object name does not follow pattern");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getCreateIndexChange(null))
            .hasExactlyViolationsMessages("Object name does not follow pattern");
    }

    @DisplayName("Object name length rule should support formatted error message with length arg")
    @Test
    void objectNameRuleShouldReturnFormattedErrorMessage() {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withPattern("^(?!_)[A-Z_0-9]+(?<!_)$")
            .withErrorMessage("Object name '%s' must follow pattern '%s'")
            .build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddColumnChange("&VALUE"))
            .hasExactlyViolationsMessages("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddForeignKeyConstraintChange("&VALUE"))
            .hasExactlyViolationsMessages("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddPrimaryKeyConstraintChange("&VALUE"))
            .hasExactlyViolationsMessages("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddUniqueConstraintChange("&VALUE"))
            .hasExactlyViolationsMessages("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getCreateTableChange("&VALUE"))
            .hasExactlyViolationsMessages("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getMergeColumnChange("&VALUE"))
            .hasExactlyViolationsMessages("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getRenameColumnChange("&VALUE"))
            .hasExactlyViolationsMessages("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getRenameViewChange("&VALUE"))
            .hasExactlyViolationsMessages("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getCreateViewChange("&VALUE"))
            .hasExactlyViolationsMessages("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getCreateIndexChange("&VALUE"))
            .hasExactlyViolationsMessages("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
    }

    @DisplayName("Object name length rule should support formatted error message with comma separated multiple errors")
    @Test
    void objectNameRuleShouldReturnFormattedErrorMessageWithCommaSeparatedMultipleErrors() {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withPattern("^(?!_)[A-Z_0-9]+(?<!_)$")
            .withErrorMessage("Object name '%s' must follow pattern '%s'")
            .build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getAddColumnChange("&VALUE", "&VALUE2"))
            .hasExactlyViolationsMessages(
                "Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'",
                "Object name '&VALUE2' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'"
            );
    }

    private AddColumnChange getAddColumnChange(String... columnNames) {
        AddColumnChange addColumnChange = new AddColumnChange();
        if (columnNames != null) {
            for (String columnName : columnNames) {
                AddColumnConfig addColumnConfig = new AddColumnConfig();
                addColumnConfig.setName(columnName);
                addColumnChange.getColumns().add(addColumnConfig);
            }
        }
        return addColumnChange;
    }

    private AddForeignKeyConstraintChange getAddForeignKeyConstraintChange(String constraintName) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setConstraintName(constraintName);
        addForeignKeyConstraintChange.setBaseTableName("BASE");
        addForeignKeyConstraintChange.setReferencedTableName("REFERENCED");
        return addForeignKeyConstraintChange;
    }

    private AddPrimaryKeyChange getAddPrimaryKeyConstraintChange(String constraintName) {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setConstraintName(constraintName);
        addPrimaryKeyChange.setTableName("VALUE");
        return addPrimaryKeyChange;
    }

    private AddUniqueConstraintChange getAddUniqueConstraintChange(String constraintName) {
        AddUniqueConstraintChange addUniqueConstraintChange = new AddUniqueConstraintChange();
        addUniqueConstraintChange.setConstraintName(constraintName);
        return addUniqueConstraintChange;
    }

    private CreateTableChange getCreateTableChange(String columnName) {
        CreateTableChange createTableChange = new CreateTableChange();
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setName(columnName);
        createTableChange.getColumns().add(addColumnConfig);
        return createTableChange;
    }

    private MergeColumnChange getMergeColumnChange(String columnName) {
        MergeColumnChange mergeColumnChange = new MergeColumnChange();
        mergeColumnChange.setFinalColumnName(columnName);
        return mergeColumnChange;
    }

    private RenameColumnChange getRenameColumnChange(String columnName) {
        RenameColumnChange renameColumnChange = new RenameColumnChange();
        renameColumnChange.setNewColumnName(columnName);
        return renameColumnChange;
    }

    private RenameViewChange getRenameViewChange(String viewName) {
        RenameViewChange renameViewChange = new RenameViewChange();
        renameViewChange.setNewViewName(viewName);
        return renameViewChange;
    }

    private CreateViewChange getCreateViewChange(String viewName) {
        CreateViewChange createViewChange = new CreateViewChange();
        createViewChange.setViewName(viewName);
        return createViewChange;
    }

    private CreateIndexChange getCreateIndexChange(String indexName) {
        CreateIndexChange createViewChange = new CreateIndexChange();
        createViewChange.setIndexName(indexName);
        return createViewChange;
    }
}
