package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.rules.core.SchemaNameRules.NoSchemaNameRule;
import liquibase.change.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoSchemaNameRuleTest {

    private NoSchemaNameRule noSchemaNameRule;

    @BeforeEach
    void setUp() {
        noSchemaNameRule = new NoSchemaNameRule();
    }

    @DisplayName("Schema name should be null")
    @Test
    void schemaNameShouldBeNull() {
        assertThat(noSchemaNameRule.invalid(getAddColumnChange("SCHEMA_NAME"))).isTrue();
        assertThat(noSchemaNameRule.invalid(getAddForeignKeyConstraintChange("SCHEMA_NAME", "SCHEMA_NAME"))).isTrue();
        assertThat(noSchemaNameRule.invalid(getAddPrimaryKeyConstraintChange("SCHEMA_NAME"))).isTrue();
        assertThat(noSchemaNameRule.invalid(getAddUniqueConstraintChange("SCHEMA_NAME"))).isTrue();
        assertThat(noSchemaNameRule.invalid(getCreateTableChange("SCHEMA_NAME"))).isTrue();
        assertThat(noSchemaNameRule.invalid(getMergeColumnChange("SCHEMA_NAME"))).isTrue();
        assertThat(noSchemaNameRule.invalid(getRenameColumnChange("SCHEMA_NAME"))).isTrue();
        assertThat(noSchemaNameRule.invalid(getRenameViewChange("SCHEMA_NAME"))).isTrue();
        assertThat(noSchemaNameRule.invalid(getCreateViewChange("SCHEMA_NAME"))).isTrue();
        assertThat(noSchemaNameRule.invalid(getCreateIndexChange("SCHEMA_NAME"))).isTrue();
    }

    @DisplayName("Schema name null should be valid")
    @Test
    void schemaNameNullShouldBeValid() {
        assertThat(noSchemaNameRule.invalid(getAddColumnChange(null))).isFalse();
        assertThat(noSchemaNameRule.invalid(getAddForeignKeyConstraintChange(null, null))).isFalse();
        assertThat(noSchemaNameRule.invalid(getAddPrimaryKeyConstraintChange(null))).isFalse();
        assertThat(noSchemaNameRule.invalid(getAddUniqueConstraintChange(null))).isFalse();
        assertThat(noSchemaNameRule.invalid(getCreateTableChange(null))).isFalse();
        assertThat(noSchemaNameRule.invalid(getMergeColumnChange(null))).isFalse();
        assertThat(noSchemaNameRule.invalid(getRenameColumnChange(null))).isFalse();
        assertThat(noSchemaNameRule.invalid(getRenameViewChange(null))).isFalse();
        assertThat(noSchemaNameRule.invalid(getCreateViewChange(null))).isFalse();
        assertThat(noSchemaNameRule.invalid(getCreateIndexChange(null))).isFalse();
    }

    @DisplayName("Schema name empty should be valid")
    @Test
    void schemaNameEmptyShouldBeValid() {
        assertThat(noSchemaNameRule.invalid(getAddColumnChange(""))).isFalse();
        assertThat(noSchemaNameRule.invalid(getAddForeignKeyConstraintChange("", ""))).isFalse();
        assertThat(noSchemaNameRule.invalid(getAddPrimaryKeyConstraintChange(""))).isFalse();
        assertThat(noSchemaNameRule.invalid(getAddUniqueConstraintChange(""))).isFalse();
        assertThat(noSchemaNameRule.invalid(getCreateTableChange(""))).isFalse();
        assertThat(noSchemaNameRule.invalid(getMergeColumnChange(""))).isFalse();
        assertThat(noSchemaNameRule.invalid(getRenameColumnChange(""))).isFalse();
        assertThat(noSchemaNameRule.invalid(getRenameViewChange(""))).isFalse();
        assertThat(noSchemaNameRule.invalid(getCreateViewChange(""))).isFalse();
        assertThat(noSchemaNameRule.invalid(getCreateIndexChange(""))).isFalse();
    }

    private AddColumnChange getAddColumnChange(String schemaName) {
        AddColumnChange addColumnChange = new AddColumnChange();
        addColumnChange.setSchemaName(schemaName);
        return addColumnChange;
    }

    private AddForeignKeyConstraintChange getAddForeignKeyConstraintChange(String baseSchemaName, String referenceSchemaName) {
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
