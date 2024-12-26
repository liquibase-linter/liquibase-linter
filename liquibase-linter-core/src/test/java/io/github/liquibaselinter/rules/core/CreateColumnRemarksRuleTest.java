package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import liquibase.change.AddColumnConfig;
import liquibase.change.core.AddColumnChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateColumnRemarksRuleTest {

    private final CreateColumnRemarksRule rule = new CreateColumnRemarksRule();

    @DisplayName("Should not allow create column without remarks attribute")
    @Test
    void shouldNotAllowCreateColumnWithoutRemarks() {
        assertThat(rule)
            .checkingChange(addColumnChange(null))
            .hasExactlyViolationsMessages("Add column must contain remarks");
    }

    @DisplayName("Should not allow create column with empty attribute")
    @Test
    void shouldNotAllowCreateColumnWithEmptyRemarks() {
        assertThat(rule)
            .checkingChange(addColumnChange(""))
            .hasExactlyViolationsMessages("Add column must contain remarks");
    }

    @DisplayName("Should allow create column with remarks attribute")
    @Test
    void shouldAllowCreateColumnWithRemarks() {
        assertThat(rule).checkingChange(addColumnChange("Some remarks")).hasNoViolations();
    }

    private AddColumnChange addColumnChange(String remarks) {
        AddColumnConfig column = new AddColumnConfig();
        column.setName("column_name");
        column.setRemarks(remarks);

        AddColumnChange change = new AddColumnChange();
        change.addColumn(column);
        return change;
    }
}
