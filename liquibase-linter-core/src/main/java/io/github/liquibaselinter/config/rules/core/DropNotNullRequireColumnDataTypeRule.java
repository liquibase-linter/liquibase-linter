package io.github.liquibaselinter.config.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.rules.AbstractLintRule;
import io.github.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.core.DropNotNullConstraintChange;

@SuppressWarnings("rawtypes")
@AutoService({ChangeRule.class})
public class DropNotNullRequireColumnDataTypeRule extends AbstractLintRule implements ChangeRule<DropNotNullConstraintChange> {
    private static final String NAME = "drop-not-null-require-column-data-type";
    private static final String MESSAGE = "Drop not null constraint column data type attribute must be populated";

    public DropNotNullRequireColumnDataTypeRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<DropNotNullConstraintChange> getChangeType() {
        return DropNotNullConstraintChange.class;
    }

    @Override
    public boolean invalid(DropNotNullConstraintChange dropNotNullConstraintChange) {
        return checkNotBlank(dropNotNullConstraintChange.getColumnDataType());
    }

}
