package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.RenameTableChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableNameRuleTest {

    private TableNameRule tableNameRule;

    @BeforeEach
    void setUp() {
        tableNameRule = new TableNameRule();
    }

    @DisplayName("Table name must not be null")
    @Test
    void tableNameNameMustNotBeNull() {
        assertThat(tableNameRule.invalid(getCreateTableChange(null))).isTrue();
        assertThat(tableNameRule.invalid(getRenameTableChange(null))).isTrue();
    }

    @DisplayName("Table name must follow pattern")
    @Test
    void tableNameNameMustFollowPattern() {
        tableNameRule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").build());

        assertThat(tableNameRule.invalid(getCreateTableChange("TBL_INVALID"))).isTrue();
        assertThat(tableNameRule.invalid(getRenameTableChange("TBL_INVALID"))).isTrue();

        assertThat(tableNameRule.invalid(getCreateTableChange("TABLE_VALID"))).isFalse();
        assertThat(tableNameRule.invalid(getRenameTableChange("TABLE_VALID"))).isFalse();
    }

    @DisplayName("Table name rule should support formatted error message with pattern arg")
    @Test
    void tableNameNameRuleShouldReturnFormattedErrorMessage() {
        tableNameRule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").withErrorMessage("Table name '%s' must follow pattern '%s'").build());
        assertThat(tableNameRule.getMessage(getCreateTableChange("TBL_INVALID"))).isEqualTo("Table name 'TBL_INVALID' must follow pattern '^(?!TBL)[A-Z_]+(?<!_)$'");
        assertThat(tableNameRule.getMessage(getRenameTableChange("TBL_INVALID"))).isEqualTo("Table name 'TBL_INVALID' must follow pattern '^(?!TBL)[A-Z_]+(?<!_)$'");
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
