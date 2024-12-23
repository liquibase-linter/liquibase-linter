package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import liquibase.change.AddColumnConfig;
import liquibase.change.core.AddColumnChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateColumnRemarksRuleTest {

    private final CreateColumnRemarksRule rule = new CreateColumnRemarksRule();

    @DisplayName("Should not allow create column without remarks attribute")
    @Test
    void shouldNotAllowCreateColumnWithoutRemarks() {
        assertThat(rule.invalid(buildAddColumnChange(null))).isTrue();
    }

    @DisplayName("Should not allow create column with empty attribute")
    @Test
    void shouldNotAllowCreateColumnWithEmptyRemarks() {
        assertThat(rule.invalid(buildAddColumnChange(""))).isTrue();
    }

    @DisplayName("Should allow create column with remarks attribute")
    @Test
    void shouldAllowCreateColumnWithRemarks() {
        assertThat(rule.invalid(buildAddColumnChange("Some remarks"))).isFalse();
    }

    private AddColumnChange buildAddColumnChange(String remarks) {
        AddColumnChange addColumnChange = new AddColumnChange();
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setRemarks(remarks);
        addColumnChange.getColumns().add(addColumnConfig);
        return addColumnChange;
    }
}
