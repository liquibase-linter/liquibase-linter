package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.rules.core.SchemaNameRules.NoSchemaNameRule;
import liquibase.change.core.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NoSchemaNameRuleTest {

    private final NoSchemaNameRule rule = new NoSchemaNameRule();

    @DisplayName("Schema name should be null")
    @Test
    void schemaNameShouldBeNull() {
        assertThat(rule.invalid(getAddColumnChange("SCHEMA_NAME"))).isTrue();
        assertThat(rule.invalid(getAddForeignKeyConstraintChange("SCHEMA_NAME", "SCHEMA_NAME"))).isTrue();
        assertThat(rule.invalid(getAddPrimaryKeyConstraintChange("SCHEMA_NAME"))).isTrue();
        assertThat(rule.invalid(getAddUniqueConstraintChange("SCHEMA_NAME"))).isTrue();
        assertThat(rule.invalid(getCreateTableChange("SCHEMA_NAME"))).isTrue();
        assertThat(rule.invalid(getMergeColumnChange("SCHEMA_NAME"))).isTrue();
        assertThat(rule.invalid(getRenameColumnChange("SCHEMA_NAME"))).isTrue();
        assertThat(rule.invalid(getRenameViewChange("SCHEMA_NAME"))).isTrue();
        assertThat(rule.invalid(getCreateViewChange("SCHEMA_NAME"))).isTrue();
        assertThat(rule.invalid(getCreateIndexChange("SCHEMA_NAME"))).isTrue();
    }

    @DisplayName("Schema name null should be valid")
    @Test
    void schemaNameNullShouldBeValid() {
        assertThat(rule.invalid(getAddColumnChange(null))).isFalse();
        assertThat(rule.invalid(getAddForeignKeyConstraintChange(null, null))).isFalse();
        assertThat(rule.invalid(getAddPrimaryKeyConstraintChange(null))).isFalse();
        assertThat(rule.invalid(getAddUniqueConstraintChange(null))).isFalse();
        assertThat(rule.invalid(getCreateTableChange(null))).isFalse();
        assertThat(rule.invalid(getMergeColumnChange(null))).isFalse();
        assertThat(rule.invalid(getRenameColumnChange(null))).isFalse();
        assertThat(rule.invalid(getRenameViewChange(null))).isFalse();
        assertThat(rule.invalid(getCreateViewChange(null))).isFalse();
        assertThat(rule.invalid(getCreateIndexChange(null))).isFalse();
    }

    @DisplayName("Schema name empty should be valid")
    @Test
    void schemaNameEmptyShouldBeValid() {
        assertThat(rule.invalid(getAddColumnChange(""))).isFalse();
        assertThat(rule.invalid(getAddForeignKeyConstraintChange("", ""))).isFalse();
        assertThat(rule.invalid(getAddPrimaryKeyConstraintChange(""))).isFalse();
        assertThat(rule.invalid(getAddUniqueConstraintChange(""))).isFalse();
        assertThat(rule.invalid(getCreateTableChange(""))).isFalse();
        assertThat(rule.invalid(getMergeColumnChange(""))).isFalse();
        assertThat(rule.invalid(getRenameColumnChange(""))).isFalse();
        assertThat(rule.invalid(getRenameViewChange(""))).isFalse();
        assertThat(rule.invalid(getCreateViewChange(""))).isFalse();
        assertThat(rule.invalid(getCreateIndexChange(""))).isFalse();
    }

    private AddColumnChange getAddColumnChange(String schemaName) {
        AddColumnChange addColumnChange = new AddColumnChange();
        addColumnChange.setSchemaName(schemaName);
        return addColumnChange;
    }

    private AddForeignKeyConstraintChange getAddForeignKeyConstraintChange(
        String baseSchemaName,
        String referenceSchemaName
    ) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
        addForeignKeyConstraintChange.setBaseTableSchemaName(baseSchemaName);
        addForeignKeyConstraintChange.setReferencedTableSchemaName(referenceSchemaName);
        return addForeignKeyConstraintChange;
    }

    private AddPrimaryKeyChange getAddPrimaryKeyConstraintChange(String schemaName) {
        AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
        addPrimaryKeyChange.setSchemaName(schemaName);
        return addPrimaryKeyChange;
    }

    private AddUniqueConstraintChange getAddUniqueConstraintChange(String schemaName) {
        AddUniqueConstraintChange addUniqueConstraintChange = new AddUniqueConstraintChange();
        addUniqueConstraintChange.setSchemaName(schemaName);
        return addUniqueConstraintChange;
    }

    private CreateTableChange getCreateTableChange(String schemaName) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setSchemaName(schemaName);
        return createTableChange;
    }

    private MergeColumnChange getMergeColumnChange(String schemaName) {
        MergeColumnChange mergeColumnChange = new MergeColumnChange();
        mergeColumnChange.setSchemaName(schemaName);
        return mergeColumnChange;
    }

    private RenameColumnChange getRenameColumnChange(String schemaName) {
        RenameColumnChange renameColumnChange = new RenameColumnChange();
        renameColumnChange.setSchemaName(schemaName);
        return renameColumnChange;
    }

    private RenameViewChange getRenameViewChange(String schemaName) {
        RenameViewChange renameViewChange = new RenameViewChange();
        renameViewChange.setSchemaName(schemaName);
        return renameViewChange;
    }

    private CreateViewChange getCreateViewChange(String schemaName) {
        CreateViewChange createViewChange = new CreateViewChange();
        createViewChange.setSchemaName(schemaName);
        return createViewChange;
    }

    private CreateIndexChange getCreateIndexChange(String schemaName) {
        CreateIndexChange createViewChange = new CreateIndexChange();
        createViewChange.setSchemaName(schemaName);
        return createViewChange;
    }
}
