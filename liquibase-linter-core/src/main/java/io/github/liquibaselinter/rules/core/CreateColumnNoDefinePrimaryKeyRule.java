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
@AutoService(ChangeRule.class)
public class CreateColumnNoDefinePrimaryKeyRule extends AbstractLintRule implements ChangeRule<Change> {
    private static final String NAME = "create-column-no-define-primary-key";
    private static final String MESSAGE = "Add column must not use primary key attribute. Instead use AddPrimaryKey change type";

    public CreateColumnNoDefinePrimaryKeyRule() {
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
        for (ColumnConfig columnConfig : (List<ColumnConfig>) changeWithColumns.getColumns()) {
            ConstraintsConfig constraints = columnConfig.getConstraints();
            if (constraints != null && Boolean.TRUE.equals(constraints.isPrimaryKey())) {
                return true;
            }
        }
        return false;
    }

}
