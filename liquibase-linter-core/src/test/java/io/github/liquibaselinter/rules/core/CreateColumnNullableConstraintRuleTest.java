package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import java.util.Arrays;
import liquibase.change.AddColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CreateColumnNullableConstraintRuleTest {

    private final CreateColumnNullableConstraintRule rule = new CreateColumnNullableConstraintRule();

    @Nested
    class AddColumn {

        @DisplayName("Null constraints should be invalid")
        @Test
        void nullConstraintsShouldBeInvalid() {
            AddColumnChange addColumnChange = addColumnChangeWithConstraints(null);
            assertThat(rule)
                .checkingChange(addColumnChange)
                .hasExactlyViolationsMessages("Add column must specify nullable constraint");
        }

        @DisplayName("Null nullable attribute should be invalid")
        @Test
        void nullNullableAttributeShouldBeInvalid() {
            ConstraintsConfig constraintsConfig = new ConstraintsConfig();
            AddColumnChange addColumnChange = addColumnChangeWithConstraints(constraintsConfig);

            assertThat(rule)
                .checkingChange(addColumnChange)
                .hasExactlyViolationsMessages("Add column must specify nullable constraint");
        }

        @DisplayName("Not null nullable attribute should be valid")
        @Test
        void notNullNullableAttributeShouldBeValid() {
            ConstraintsConfig constraintsConfig = new ConstraintsConfig();
            constraintsConfig.setNullable(Boolean.TRUE);
            AddColumnChange addColumnChange = addColumnChangeWithConstraints(constraintsConfig);

            assertThat(rule).checkingChange(addColumnChange).hasNoViolations();
        }

        private AddColumnChange addColumnChangeWithConstraints(ConstraintsConfig constraintsConfig) {
            AddColumnConfig column = column("column_name", constraintsConfig);

            AddColumnChange change = new AddColumnChange();
            change.addColumn(column);
            return change;
        }
    }

    @Nested
    class CreateTable {

        @Test
        @DisplayName("Null constraints should be invalid")
        void nullConstraintsShouldBeInvalid() {
            ConstraintsConfig constraintsConfig = new ConstraintsConfig();
            constraintsConfig.setNullable(Boolean.TRUE);
            CreateTableChange change = createTableWithConstraints(
                column("column_name", null),
                column("column_name", constraintsConfig)
            );

            assertThat(rule)
                .checkingChange(change)
                .hasExactlyViolationsMessages("Add column must specify nullable constraint");
        }

        private CreateTableChange createTableWithConstraints(AddColumnConfig... columns) {
            CreateTableChange change = new CreateTableChange();
            Arrays.stream(columns).forEach(change::addColumn);
            return change;
        }
    }

    private static AddColumnConfig column(String columnName, ConstraintsConfig constraintsConfig) {
        AddColumnConfig column = new AddColumnConfig();
        column.setName(columnName);
        column.setConstraints(constraintsConfig);
        return column;
    }
}
