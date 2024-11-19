package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.core.AddUniqueConstraintChange;

@SuppressWarnings("rawtypes")
@AutoService(ChangeRule.class)
public class UniqueConstraintNameRule extends AbstractLintRule implements ChangeRule<AddUniqueConstraintChange> {
    private static final String NAME = "unique-constraint-name";
    private static final String MESSAGE = "Unique constraint name does not follow pattern";

    public UniqueConstraintNameRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean supports(Change change) {
        return change instanceof AddUniqueConstraintChange;
    }

    @Override
    public boolean invalid(AddUniqueConstraintChange change) {
        return checkMandatoryPattern(change.getConstraintName(), change);
    }

    @Override
    public String getMessage(AddUniqueConstraintChange change) {
        return formatMessage(change.getConstraintName(), getConfig().getPatternString());
    }

}
