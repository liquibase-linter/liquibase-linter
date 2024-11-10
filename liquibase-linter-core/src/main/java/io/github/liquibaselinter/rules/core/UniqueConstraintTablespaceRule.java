package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.core.AddUniqueConstraintChange;

@SuppressWarnings("rawtypes")
@AutoService(ChangeRule.class)
public class UniqueConstraintTablespaceRule extends AbstractLintRule implements ChangeRule<AddUniqueConstraintChange> {
    private static final String NAME = "unique-constraint-tablespace";
    private static final String MESSAGE = "Tablespace '%s' of unique constraint '%s' is empty or does not follow pattern '%s'";

    public UniqueConstraintTablespaceRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<AddUniqueConstraintChange> getChangeType() {
        return AddUniqueConstraintChange.class;
    }

    @Override
    public boolean invalid(AddUniqueConstraintChange change) {
        return checkMandatoryPattern(change.getTablespace(), change);
    }

    @Override
    public String getMessage(AddUniqueConstraintChange change) {
        return formatMessage(change.getTablespace(), change.getConstraintName(), getPatternForMessage(change));
    }

}
