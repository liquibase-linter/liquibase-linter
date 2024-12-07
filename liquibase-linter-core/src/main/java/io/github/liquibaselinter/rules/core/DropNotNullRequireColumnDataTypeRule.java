package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.core.DropNotNullConstraintChange;

@AutoService(ChangeRule.class)
public class DropNotNullRequireColumnDataTypeRule extends AbstractLintRule implements ChangeRule {

    private static final String NAME = "drop-not-null-require-column-data-type";
    private static final String MESSAGE = "Drop not null constraint column data type attribute must be populated";

    public DropNotNullRequireColumnDataTypeRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean supports(Change change) {
        return change instanceof DropNotNullConstraintChange;
    }

    @Override
    public boolean invalid(Change change) {
        DropNotNullConstraintChange dropNotNullConstraintChange = (DropNotNullConstraintChange) change;
        return checkNotBlank(dropNotNullConstraintChange.getColumnDataType());
    }
}
