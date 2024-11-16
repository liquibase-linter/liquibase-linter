package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.ChangeWithColumns;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;

import java.util.List;

@SuppressWarnings("rawtypes")
@AutoService({ChangeRule.class})
public class CreateColumnNullableConstraintRule extends AbstractLintRule implements ChangeRule<Change> {
    private static final String NAME = "create-column-nullable-constraint";
    private static final String MESSAGE = "Add column must specify nullable constraint";

    public CreateColumnNullableConstraintRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<Change> getChangeType() {
        return Change.class;
    }

    @Override
    public boolean supports(Change change) {
        return change instanceof CreateTableChange || change instanceof AddColumnChange;
    }

    @Override
    public boolean invalid(Change change) {
        ChangeWithColumns changeWithColumns = (ChangeWithColumns) change;
        for (ColumnConfig column : (List<ColumnConfig>) changeWithColumns.getColumns()) {
            final ConstraintsConfig constraints = column.getConstraints();
            if (constraints == null || constraints.isNullable() == null) {
                return true;
            }
        }
        return false;
    }


}
