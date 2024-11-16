package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.core.ObjectNameRules.ObjectNameLengthRule;
import liquibase.change.AddColumnConfig;
import liquibase.change.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectNameLengthRuleTest {

    private ObjectNameLengthRule objectNameLengthRule;

    @BeforeEach
    void setUp() {
        objectNameLengthRule = new ObjectNameLengthRule();
    }

    @DisplayName("Object name must not exceed max length")
    @Test
    void objectNameMustNotExceedMaxLength() {
        objectNameLengthRule.configure(RuleConfig.builder().withMaxLength(4).build());
        assertThat(objectNameLengthRule.invalid(getAddColumnChange("VALUE"))).isTrue();
        assertThat(objectNameLengthRule.invalid(getAddForeignKeyConstraintChange("VALUE"))).isTrue();
        assertThat(objectNameLengthRule.invalid(getAddPrimaryKeyConstraintChange("VALUE"))).isTrue();
        assertThat(objectNameLengthRule.invalid(getAddUniqueConstraintChange("VALUE"))).isTrue();
        assertThat(objectNameLengthRule.invalid(getCreateTableChange("VALUE"))).isTrue();
        assertThat(objectNameLengthRule.invalid(getMergeColumnChange("VALUE"))).isTrue();
        assertThat(objectNameLengthRule.invalid(getRenameColumnChange("VALUE"))).isTrue();
        assertThat(objectNameLengthRule.invalid(getRenameViewChange("VALUE"))).isTrue();
        assertThat(objectNameLengthRule.invalid(getCreateViewChange("VALUE"))).isTrue();
        assertThat(objectNameLengthRule.invalid(getCreateIndexChange("VALUE"))).isTrue();
    }

    @DisplayName("Object name can equal max length")
    @Test
    void tableLengthCanEqualMaxLength() {
        objectNameLengthRule.configure(RuleConfig.builder().withMaxLength(5).build());
        assertThat(objectNameLengthRule.invalid(getAddColumnChange("VALUE"))).isFalse();
        assertThat(objectNameLengthRule.invalid(getAddForeignKeyConstraintChange("VALUE"))).isFalse();
        assertThat(objectNameLengthRule.invalid(getAddPrimaryKeyConstraintChange("VALUE"))).isFalse();
        assertThat(objectNameLengthRule.invalid(getAddUniqueConstraintChange("VALUE"))).isFalse();
        assertThat(objectNameLengthRule.invalid(getCreateTableChange("VALUE"))).isFalse();
        assertThat(objectNameLengthRule.invalid(getMergeColumnChange("VALUE"))).isFalse();
        assertThat(objectNameLengthRule.invalid(getRenameColumnChange("VALUE"))).isFalse();
        assertThat(objectNameLengthRule.invalid(getRenameViewChange("VALUE"))).isFalse();
        assertThat(objectNameLengthRule.invalid(getCreateViewChange("VALUE"))).isFalse();
        assertThat(objectNameLengthRule.invalid(getCreateIndexChange("VALUE"))).isFalse();
    }

    @DisplayName("Object name can be null")
    @Test
    void objectNameCanBeNull() {
        objectNameLengthRule.configure(RuleConfig.builder().withMaxLength(4).build());
        assertThat(objectNameLengthRule.invalid(getAddColumnChange((String) null))).isFalse();
        assertThat(objectNameLengthRule.invalid(getAddForeignKeyConstraintChange(null))).isFalse();
        assertThat(objectNameLengthRule.invalid(getAddPrimaryKeyConstraintChange(null))).isFalse();
        assertThat(objectNameLengthRule.invalid(getAddUniqueConstraintChange(null))).isFalse();
        assertThat(objectNameLengthRule.invalid(getCreateTableChange(null))).isFalse();
        assertThat(objectNameLengthRule.invalid(getMergeColumnChange(null))).isFalse();
        assertThat(objectNameLengthRule.invalid(getRenameColumnChange(null))).isFalse();
        assertThat(objectNameLengthRule.invalid(getRenameViewChange(null))).isFalse();
        assertThat(objectNameLengthRule.invalid(getCreateViewChange(null))).isFalse();
        assertThat(objectNameLengthRule.invalid(getCreateIndexChange(null))).isFalse();
    }

    @DisplayName("Object name length rule should support formatted error message with length arg")
    @Test
    void objectNameLengthRuleShouldReturnFormattedErrorMessage() {
        objectNameLengthRule.configure(RuleConfig.builder().withMaxLength(4).withErrorMessage("Object name '%s' must be less than %d characters").build());

        assertThat(objectNameLengthRule.getMessage(getAddColumnChange("VALUE"))).isEqualTo("Object name 'VALUE' must be less than 4 characters");
        assertThat(objectNameLengthRule.getMessage(getAddForeignKeyConstraintChange("VALUE"))).isEqualTo("Object name 'VALUE' must be less than 4 characters");
        assertThat(objectNameLengthRule.getMessage(getAddPrimaryKeyConstraintChange("VALUE"))).isEqualTo("Object name 'VALUE' must be less than 4 characters");
        assertThat(objectNameLengthRule.getMessage(getAddUniqueConstraintChange("VALUE"))).isEqualTo("Object name 'VALUE' must be less than 4 characters");
        assertThat(objectNameLengthRule.getMessage(getCreateTableChange("VALUE"))).isEqualTo("Object name 'VALUE' must be less than 4 characters");
        assertThat(objectNameLengthRule.getMessage(getMergeColumnChange("VALUE"))).isEqualTo("Object name 'VALUE' must be less than 4 characters");
        assertThat(objectNameLengthRule.getMessage(getRenameColumnChange("VALUE"))).isEqualTo("Object name 'VALUE' must be less than 4 characters");
        assertThat(objectNameLengthRule.getMessage(getRenameViewChange("VALUE"))).isEqualTo("Object name 'VALUE' must be less than 4 characters");
        assertThat(objectNameLengthRule.getMessage(getCreateViewChange("VALUE"))).isEqualTo("Object name 'VALUE' must be less than 4 characters");
        assertThat(objectNameLengthRule.getMessage(getCreateIndexChange("VALUE"))).isEqualTo("Object name 'VALUE' must be less than 4 characters");
    }

    @DisplayName("Object name length rule should support formatted error message with comma separated multiple errors")
    @Test
    void objectNameLengthRuleShouldReturnFormattedErrorMessageWithCommaSeparatedMultipleErrors() {
        objectNameLengthRule.configure(RuleConfig.builder().withMaxLength(4).withErrorMessage("Object name '%s' must be less than %d characters").build());

        assertThat(objectNameLengthRule.getMessage(getAddColumnChange("VALUE", "VALUE2"))).isEqualTo("Object name 'VALUE,VALUE2' must be less than 4 characters");

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
