package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.rules.core.CreateColumnNullableConstraintRule;
import liquibase.change.AddColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddColumnChange;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateColumnNullableConstraintRuleTest {

    private CreateColumnNullableConstraintRule createColumnNullableConstraint;

    @BeforeEach
    void setUp() {
        createColumnNullableConstraint = new CreateColumnNullableConstraintRule();
    }

    @DisplayName("Null constraints should be invalid")
    @Test
    void nullConstraintsShouldBeInvalid() {
        final AddColumnChange addColumnChange = mockAddColumnChangeWithConstraints(null);
        assertThat(createColumnNullableConstraint.invalid(addColumnChange)).isTrue();
    }

    @DisplayName("Null nullable attribute should be invalid")
    @Test
    void nullNullableAttributeShouldBeInvalid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        final AddColumnChange addColumnChange = mockAddColumnChangeWithConstraints(constraintsConfig);
        assertThat(constraintsConfig.isNullable()).isNull();
        assertThat(createColumnNullableConstraint.invalid(addColumnChange)).isTrue();
    }

    @DisplayName("Not null nullable attribute should be valid")
    @Test
    void notNullNullableAttributeShouldBeValid() {
        ConstraintsConfig constraintsConfig = new ConstraintsConfig();
        constraintsConfig.setNullable(Boolean.TRUE);
        final AddColumnChange addColumnChange = mockAddColumnChangeWithConstraints(constraintsConfig);
        assertThat(constraintsConfig.isNullable()).isTrue();
        assertThat(createColumnNullableConstraint.invalid(addColumnChange)).isFalse();
    }

    private AddColumnChange mockAddColumnChangeWithConstraints(ConstraintsConfig constraintsConfig) {
        AddColumnChange addColumnChange = mock(AddColumnChange.class);
        final AddColumnConfig addColumnConfig = new AddColumnConfig();
        addColumnConfig.setConstraints(constraintsConfig);
        when(addColumnChange.getColumns()).thenReturn(Collections.singletonList(addColumnConfig));
        return addColumnChange;
    }

}
