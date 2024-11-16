package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.rules.core.CreateTableRemarksRule;
import liquibase.change.core.CreateTableChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateTableRemarksRuleTest {

    private CreateTableRemarksRule createTableRemarksRule;

    @BeforeEach
    void setUp() {
        createTableRemarksRule = new CreateTableRemarksRule();
    }

    @DisplayName("Should not allow create table without remarks attribute")
    @Test
    void shouldNotAllowCreteTableWithoutRemarks() {
        assertThat(createTableRemarksRule.invalid(getCreateTableChange(null))).isTrue();
    }

    @DisplayName("Should not allow create table without remarks attribute")
    @Test
    void shouldNotAllowCreateTableWithEmptyRemarks() {
        assertThat(createTableRemarksRule.invalid(getCreateTableChange(""))).isTrue();
    }

    @DisplayName("Should allow create table with remarks attribute")
    @Test
    void shouldAllowCreateTableWithRemarks() {
        assertThat(createTableRemarksRule.invalid(getCreateTableChange("REMARK"))).isFalse();
    }

    private CreateTableChange getCreateTableChange(String remarks) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST");
        createTableChange.setRemarks(remarks);
        return createTableChange;
    }

}
