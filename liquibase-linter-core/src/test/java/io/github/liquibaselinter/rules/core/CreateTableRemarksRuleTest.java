package io.github.liquibaselinter.rules.core;

import liquibase.change.core.CreateTableChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateTableRemarksRuleTest {

    private final CreateTableRemarksRule rule = new CreateTableRemarksRule();

    @DisplayName("Should not allow create table without remarks attribute")
    @Test
    void shouldNotAllowCreteTableWithoutRemarks() {
        assertThat(rule.invalid(getCreateTableChange(null))).isTrue();
    }

    @DisplayName("Should not allow create table without remarks attribute")
    @Test
    void shouldNotAllowCreateTableWithEmptyRemarks() {
        assertThat(rule.invalid(getCreateTableChange(""))).isTrue();
    }

    @DisplayName("Should allow create table with remarks attribute")
    @Test
    void shouldAllowCreateTableWithRemarks() {
        assertThat(rule.invalid(getCreateTableChange("REMARK"))).isFalse();
    }

    private CreateTableChange getCreateTableChange(String remarks) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST");
        createTableChange.setRemarks(remarks);
        return createTableChange;
    }

}
