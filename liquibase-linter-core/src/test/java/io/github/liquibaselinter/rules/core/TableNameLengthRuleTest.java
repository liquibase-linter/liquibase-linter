package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.RenameTableChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableNameLengthRuleTest {

    private TableNameLengthRule tableNameLengthRule;

    @BeforeEach
    void setUp() {
        tableNameLengthRule = new TableNameLengthRule();
    }

    @DisplayName("Table name must not exceed max length")
    @Test
    void tableNameMustNotExceedMaxLength() {
        tableNameLengthRule.configure(RuleConfig.builder().withMaxLength(4).build());
        assertThat(tableNameLengthRule.invalid(getCreateTableChange("TABLE"))).isTrue();
        assertThat(tableNameLengthRule.invalid(getRenameTableChange("TABLE"))).isTrue();
    }

    @DisplayName("Table name can equal max length")
    @Test
    void tableLengthCanEqualMaxLength() {
        tableNameLengthRule.configure(RuleConfig.builder().withMaxLength(5).build());
        assertThat(tableNameLengthRule.invalid(getCreateTableChange("TABLE"))).isFalse();
        assertThat(tableNameLengthRule.invalid(getRenameTableChange("TABLE"))).isFalse();
    }

    @DisplayName("Table name can be null")
    @Test
    void tableNameCanBeNull() {
        tableNameLengthRule.configure(RuleConfig.builder().withMaxLength(4).build());
        assertThat(tableNameLengthRule.invalid(getCreateTableChange(null))).isFalse();
        assertThat(tableNameLengthRule.invalid(getRenameTableChange(null))).isFalse();
    }

    @DisplayName("Table name length rule should support formatted error message with length arg")
    @Test
    void tableNameLengthRuleShouldReturnFormattedErrorMessage() {
        tableNameLengthRule.configure(RuleConfig.builder().withMaxLength(5).withErrorMessage("Table '%s' name must not be longer than %d").build());
        assertThat(tableNameLengthRule.getMessage(getCreateTableChange("TABLE_LONG"))).isEqualTo("Table 'TABLE_LONG' name must not be longer than 5");
        assertThat(tableNameLengthRule.getMessage(getRenameTableChange("TABLE_LONG"))).isEqualTo("Table 'TABLE_LONG' name must not be longer than 5");
    }

    private CreateTableChange getCreateTableChange(String tableName) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName(tableName);
        return createTableChange;
    }

    private RenameTableChange getRenameTableChange(String tableName) {
        RenameTableChange renameTableChange = new RenameTableChange();
        renameTableChange.setNewTableName(tableName);
        return renameTableChange;
    }
}
