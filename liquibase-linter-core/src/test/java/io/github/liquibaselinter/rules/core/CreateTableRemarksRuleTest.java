package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import liquibase.change.core.CreateTableChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateTableRemarksRuleTest {

    private final CreateTableRemarksRule rule = new CreateTableRemarksRule();

    @DisplayName("Should not allow create table without remarks attribute")
    @Test
    void shouldNotAllowCreteTableWithoutRemarks() {
        assertThat(rule)
            .checkingChange(getCreateTableChange(null))
            .hasExactlyViolationsMessages("Create table must contain remark attribute");
    }

    @DisplayName("Should not allow create table without remarks attribute")
    @Test
    void shouldNotAllowCreateTableWithEmptyRemarks() {
        assertThat(rule)
            .checkingChange(getCreateTableChange(""))
            .hasExactlyViolationsMessages("Create table must contain remark attribute");
    }

    @DisplayName("Should allow create table with remarks attribute")
    @Test
    void shouldAllowCreateTableWithRemarks() {
        assertThat(rule).checkingChange(getCreateTableChange("REMARK")).hasNoViolations();
    }

    private CreateTableChange getCreateTableChange(String remarks) {
        CreateTableChange createTableChange = new CreateTableChange();
        createTableChange.setTableName("TEST");
        createTableChange.setRemarks(remarks);
        return createTableChange;
    }
}
