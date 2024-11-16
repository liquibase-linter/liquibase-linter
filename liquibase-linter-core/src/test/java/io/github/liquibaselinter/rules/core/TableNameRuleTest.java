package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.RenameTableChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class TableNameRuleTest {

    private final TableNameRule rule = new TableNameRule();

    @DisplayName("Table name must not be null")
    @Test
    void tableNameNameMustNotBeNull() {
        assertThat(rule.invalid(getCreateTableChange(null))).isTrue();
        assertThat(rule.invalid(getRenameTableChange(null))).isTrue();
    }

    @DisplayName("Table name must follow pattern")
    @Test
    void tableNameNameMustFollowPattern() {
        rule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").build());

        assertThat(rule.invalid(getCreateTableChange("TBL_INVALID"))).isTrue();
        assertThat(rule.invalid(getRenameTableChange("TBL_INVALID"))).isTrue();

        assertThat(rule.invalid(getCreateTableChange("TABLE_VALID"))).isFalse();
        assertThat(rule.invalid(getRenameTableChange("TABLE_VALID"))).isFalse();
    }

    @DisplayName("Table name rule should support formatted error message with pattern arg")
    @Test
    void tableNameNameRuleShouldReturnFormattedErrorMessage() {
        rule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").withErrorMessage("Table name '%s' must follow pattern '%s'").build());
        assertThat(rule.getMessage(getCreateTableChange("TBL_INVALID"))).isEqualTo("Table name 'TBL_INVALID' must follow pattern '^(?!TBL)[A-Z_]+(?<!_)$'");
        assertThat(rule.getMessage(getRenameTableChange("TBL_INVALID"))).isEqualTo("Table name 'TBL_INVALID' must follow pattern '^(?!TBL)[A-Z_]+(?<!_)$'");
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
