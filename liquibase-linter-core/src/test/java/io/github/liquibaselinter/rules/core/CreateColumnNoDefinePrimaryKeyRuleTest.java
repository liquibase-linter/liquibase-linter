package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

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
        assertThat(rule).checkingChange(buildAddColumnChange(null)).hasNoViolations();
    }

    @DisplayName("False primary key attribute should be valid")
    @Test
    void falsePrimaryKeyAttributeShouldBeValid() {
        assertThat(rule).checkingChange(buildAddColumnChange(Boolean.FALSE)).hasNoViolations();
    }

    @DisplayName("True primary key attribute should be valid")
    @Test
    void truePrimaryKeyAttributeShouldBeInvalid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        constraintsConfig.setPrimaryKey(Boolean.TRUE);

        assertThat(rule)
            .checkingChange(buildAddColumnChange(Boolean.TRUE))
            .hasExactlyViolationsMessages(
                "Add column must not use primary key attribute. Instead use AddPrimaryKey change type"
            );
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
