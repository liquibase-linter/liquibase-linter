package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.core.AddForeignKeyConstraintChange;

@AutoService(ChangeRule.class)
public class ForeignKeyNameRule extends AbstractLintRule implements ChangeRule {

    private static final String NAME = "foreign-key-name";
    private static final String MESSAGE = "Foreign key name is missing or does not follow pattern";

    public ForeignKeyNameRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean supports(Change change) {
        return change instanceof AddForeignKeyConstraintChange;
    }

    @Override
    public boolean invalid(Change change) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = (AddForeignKeyConstraintChange) change;
        final String constraintName = addForeignKeyConstraintChange.getConstraintName();
        return checkMandatoryPattern(constraintName, addForeignKeyConstraintChange);
    }

    @Override
    public String getMessage(Change change) {
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = (AddForeignKeyConstraintChange) change;
        return formatMessage(addForeignKeyConstraintChange.getConstraintName(), ruleConfig.getPatternString());
    }
}
