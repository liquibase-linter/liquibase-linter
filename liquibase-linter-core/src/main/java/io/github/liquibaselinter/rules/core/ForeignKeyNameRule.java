package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.core.AddForeignKeyConstraintChange;

@SuppressWarnings("rawtypes")
@AutoService({ChangeRule.class})
public class ForeignKeyNameRule extends AbstractLintRule implements ChangeRule<AddForeignKeyConstraintChange> {
    private static final String NAME = "foreign-key-name";
    private static final String MESSAGE = "Foreign key name is missing or does not follow pattern";

    public ForeignKeyNameRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<AddForeignKeyConstraintChange> getChangeType() {
        return AddForeignKeyConstraintChange.class;
    }

    @Override
    public boolean invalid(AddForeignKeyConstraintChange change) {
        final String constraintName = change.getConstraintName();
        return checkMandatoryPattern(constraintName, change);
    }

    @Override
    public String getMessage(AddForeignKeyConstraintChange change) {
        return formatMessage(change.getConstraintName(), ruleConfig.getPatternString());
    }
}
