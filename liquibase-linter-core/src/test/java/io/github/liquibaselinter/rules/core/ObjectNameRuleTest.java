package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.core.ObjectNameRules.ObjectNameRule;
import liquibase.change.AddColumnConfig;
import liquibase.change.core.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectNameRuleTest {

    private final ObjectNameRule rule = new ObjectNameRule();

    @DisplayName("Object name must not be null")
    @Test
    void objectNameMustNotBeNull() {
        rule.configure(RuleConfig.builder().withPattern("^(?!_)[A-Z_0-9]+(?<!_)$").build());
        assertThat(rule.invalid(getAddColumnChange(new String[]{null}))).isTrue();
        assertThat(rule.invalid(getAddForeignKeyConstraintChange(null))).isTrue();
        assertThat(rule.invalid(getAddPrimaryKeyConstraintChange(null))).isTrue();
        assertThat(rule.invalid(getAddUniqueConstraintChange(null))).isTrue();
        assertThat(rule.invalid(getCreateTableChange(null))).isTrue();
        assertThat(rule.invalid(getMergeColumnChange(null))).isTrue();
        assertThat(rule.invalid(getRenameColumnChange(null))).isTrue();
        assertThat(rule.invalid(getRenameViewChange(null))).isTrue();
        assertThat(rule.invalid(getCreateViewChange(null))).isTrue();
        assertThat(rule.invalid(getCreateIndexChange(null))).isTrue();
    }

    @DisplayName("Object name length rule should support formatted error message with length arg")
    @Test
    void objectNameRuleShouldReturnFormattedErrorMessage() {
        rule.configure(RuleConfig.builder().withPattern("^(?!_)[A-Z_0-9]+(?<!_)$").withErrorMessage("Object name '%s' must follow pattern '%s'").build());

        assertThat(rule.getMessage(getAddColumnChange("&VALUE"))).isEqualTo("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule.getMessage(getAddForeignKeyConstraintChange("&VALUE"))).isEqualTo("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule.getMessage(getAddPrimaryKeyConstraintChange("&VALUE"))).isEqualTo("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule.getMessage(getAddUniqueConstraintChange("&VALUE"))).isEqualTo("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule.getMessage(getCreateTableChange("&VALUE"))).isEqualTo("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule.getMessage(getMergeColumnChange("&VALUE"))).isEqualTo("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule.getMessage(getRenameColumnChange("&VALUE"))).isEqualTo("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule.getMessage(getRenameViewChange("&VALUE"))).isEqualTo("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule.getMessage(getCreateViewChange("&VALUE"))).isEqualTo("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
        assertThat(rule.getMessage(getCreateIndexChange("&VALUE"))).isEqualTo("Object name '&VALUE' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");
    }

    @DisplayName("Object name length rule should support formatted error message with comma separated multiple errors")
    @Test
    void objectNameRuleShouldReturnFormattedErrorMessageWithCommaSeparatedMultipleErrors() {
        rule.configure(RuleConfig.builder().withPattern("^(?!_)[A-Z_0-9]+(?<!_)$").withErrorMessage("Object name '%s' must follow pattern '%s'").build());

        assertThat(rule.getMessage(getAddColumnChange("&VALUE", "&VALUE2"))).isEqualTo("Object name '&VALUE,&VALUE2' must follow pattern '^(?!_)[A-Z_0-9]+(?<!_)$'");

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
