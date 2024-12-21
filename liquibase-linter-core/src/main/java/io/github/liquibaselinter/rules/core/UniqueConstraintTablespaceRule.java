package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.core.AddUniqueConstraintChange;

@AutoService(ChangeRule.class)
public class UniqueConstraintTablespaceRule extends AbstractLintRule implements ChangeRule {

    private static final String NAME = "unique-constraint-tablespace";
    private static final String MESSAGE =
        "Tablespace '%s' of unique constraint '%s' is empty or does not follow pattern '%s'";

    public UniqueConstraintTablespaceRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean supports(Change change) {
        return change instanceof AddUniqueConstraintChange;
    }

    @Override
    public boolean invalid(Change change) {
        AddUniqueConstraintChange addUniqueConstraintChange = (AddUniqueConstraintChange) change;
        return checkMandatoryPattern(addUniqueConstraintChange.getTablespace(), change);
    }

    @Override
    public String getMessage(Change change) {
        AddUniqueConstraintChange addUniqueConstraintChange = (AddUniqueConstraintChange) change;
        return formatMessage(
            addUniqueConstraintChange.getTablespace(),
            addUniqueConstraintChange.getConstraintName(),
            getPatternForMessage(change)
        );
    }
}
