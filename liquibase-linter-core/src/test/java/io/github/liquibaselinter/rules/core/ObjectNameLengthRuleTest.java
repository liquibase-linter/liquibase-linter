package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.core.ObjectNameRules.ObjectNameLengthRule;
import liquibase.change.AddColumnConfig;
import liquibase.change.core.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ObjectNameLengthRuleTest {

    private final ObjectNameLengthRule rule = new ObjectNameLengthRule();

    @DisplayName("Object name must not exceed max length")
    @Test
    void objectNameMustNotExceedMaxLength() {
        rule.configure(RuleConfig.builder().withMaxLength(4).build());
        assertThat(rule.invalid(getAddColumnChange("VALUE"))).isTrue();
        assertThat(rule.invalid(getAddForeignKeyConstraintChange("VALUE"))).isTrue();
        assertThat(rule.invalid(getAddPrimaryKeyConstraintChange("VALUE"))).isTrue();
        assertThat(rule.invalid(getAddUniqueConstraintChange("VALUE"))).isTrue();
        assertThat(rule.invalid(getCreateTableChange("VALUE"))).isTrue();
        assertThat(rule.invalid(getMergeColumnChange("VALUE"))).isTrue();
        assertThat(rule.invalid(getRenameColumnChange("VALUE"))).isTrue();
        assertThat(rule.invalid(getRenameViewChange("VALUE"))).isTrue();
        assertThat(rule.invalid(getCreateViewChange("VALUE"))).isTrue();
        assertThat(rule.invalid(getCreateIndexChange("VALUE"))).isTrue();
    }

    @DisplayName("Object name can equal max length")
    @Test
    void tableLengthCanEqualMaxLength() {
        rule.configure(RuleConfig.builder().withMaxLength(5).build());
        assertThat(rule.invalid(getAddColumnChange("VALUE"))).isFalse();
        assertThat(rule.invalid(getAddForeignKeyConstraintChange("VALUE"))).isFalse();
        assertThat(rule.invalid(getAddPrimaryKeyConstraintChange("VALUE"))).isFalse();
        assertThat(rule.invalid(getAddUniqueConstraintChange("VALUE"))).isFalse();
        assertThat(rule.invalid(getCreateTableChange("VALUE"))).isFalse();
        assertThat(rule.invalid(getMergeColumnChange("VALUE"))).isFalse();
        assertThat(rule.invalid(getRenameColumnChange("VALUE"))).isFalse();
        assertThat(rule.invalid(getRenameViewChange("VALUE"))).isFalse();
        assertThat(rule.invalid(getCreateViewChange("VALUE"))).isFalse();
        assertThat(rule.invalid(getCreateIndexChange("VALUE"))).isFalse();
    }

    @DisplayName("Object name can be null")
    @Test
    void objectNameCanBeNull() {
        rule.configure(RuleConfig.builder().withMaxLength(4).build());
        assertThat(rule.invalid(getAddColumnChange((String) null))).isFalse();
        assertThat(rule.invalid(getAddForeignKeyConstraintChange(null))).isFalse();
        assertThat(rule.invalid(getAddPrimaryKeyConstraintChange(null))).isFalse();
        assertThat(rule.invalid(getAddUniqueConstraintChange(null))).isFalse();
        assertThat(rule.invalid(getCreateTableChange(null))).isFalse();
        assertThat(rule.invalid(getMergeColumnChange(null))).isFalse();
        assertThat(rule.invalid(getRenameColumnChange(null))).isFalse();
        assertThat(rule.invalid(getRenameViewChange(null))).isFalse();
        assertThat(rule.invalid(getCreateViewChange(null))).isFalse();
        assertThat(rule.invalid(getCreateIndexChange(null))).isFalse();
    }

    @DisplayName("Object name length rule should support formatted error message with length arg")
    @Test
    void objectNameLengthRuleShouldReturnFormattedErrorMessage() {
        rule.configure(
            RuleConfig.builder()
                .withMaxLength(4)
                .withErrorMessage("Object name '%s' must be less than %d characters")
                .build()
        );

        assertThat(rule.getMessage(getAddColumnChange("VALUE"))).isEqualTo(
            "Object name 'VALUE' must be less than 4 characters"
        );
        assertThat(rule.getMessage(getAddForeignKeyConstraintChange("VALUE"))).isEqualTo(
            "Object name 'VALUE' must be less than 4 characters"
        );
        assertThat(rule.getMessage(getAddPrimaryKeyConstraintChange("VALUE"))).isEqualTo(
            "Object name 'VALUE' must be less than 4 characters"
        );
        assertThat(rule.getMessage(getAddUniqueConstraintChange("VALUE"))).isEqualTo(
            "Object name 'VALUE' must be less than 4 characters"
        );
        assertThat(rule.getMessage(getCreateTableChange("VALUE"))).isEqualTo(
            "Object name 'VALUE' must be less than 4 characters"
        );
        assertThat(rule.getMessage(getMergeColumnChange("VALUE"))).isEqualTo(
            "Object name 'VALUE' must be less than 4 characters"
        );
        assertThat(rule.getMessage(getRenameColumnChange("VALUE"))).isEqualTo(
            "Object name 'VALUE' must be less than 4 characters"
        );
        assertThat(rule.getMessage(getRenameViewChange("VALUE"))).isEqualTo(
            "Object name 'VALUE' must be less than 4 characters"
        );
        assertThat(rule.getMessage(getCreateViewChange("VALUE"))).isEqualTo(
            "Object name 'VALUE' must be less than 4 characters"
        );
        assertThat(rule.getMessage(getCreateIndexChange("VALUE"))).isEqualTo(
            "Object name 'VALUE' must be less than 4 characters"
        );
    }

    @DisplayName("Object name length rule should support formatted error message with comma separated multiple errors")
    @Test
    void objectNameLengthRuleShouldReturnFormattedErrorMessageWithCommaSeparatedMultipleErrors() {
        rule.configure(
            RuleConfig.builder()
                .withMaxLength(4)
                .withErrorMessage("Object name '%s' must be less than %d characters")
                .build()
        );

        assertThat(rule.getMessage(getAddColumnChange("VALUE", "VALUE2"))).isEqualTo(
            "Object name 'VALUE,VALUE2' must be less than 4 characters"
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
