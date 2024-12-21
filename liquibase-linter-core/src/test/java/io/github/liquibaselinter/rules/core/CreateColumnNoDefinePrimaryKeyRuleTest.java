package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import liquibase.change.AddColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddColumnChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateColumnNoDefinePrimaryKeyRuleTest {

    private final CreateColumnNoDefinePrimaryKeyRule rule = new CreateColumnNoDefinePrimaryKeyRule();

    @DisplayName("Null primary key attribute should be valid")
    @Test
    void nullPrimaryKeyAttributeShouldBeValid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        assertThat(constraintsConfig.isPrimaryKey()).isNull();

        assertThat(rule.invalid(buildAddColumnChange(null))).isFalse();
    }

    @DisplayName("False primary key attribute should be valid")
    @Test
    void falsePrimaryKeyAttributeShouldBeValid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        constraintsConfig.setPrimaryKey(Boolean.FALSE);

        assertThat(rule.invalid(buildAddColumnChange(Boolean.FALSE))).isFalse();
    }

    @DisplayName("True primary key attribute should be valid")
    @Test
    void truePrimaryKeyAttributeShouldBeInvalid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        constraintsConfig.setPrimaryKey(Boolean.TRUE);

        assertThat(rule.invalid(buildAddColumnChange(Boolean.TRUE))).isTrue();
    }

    private static AddColumnChange buildAddColumnChange(Boolean primaryKey) {
        AddColumnChange addColumnChange = new AddColumnChange();
        AddColumnConfig addColumnConfig = new AddColumnConfig();
        ConstraintsConfig constraints = new ConstraintsConfig();
        constraints.setPrimaryKey(primaryKey);
        addColumnConfig.setConstraints(constraints);
        addColumnChange.getColumns().add(addColumnConfig);
        return addColumnChange;
    }
}
