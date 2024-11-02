package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.DatabaseChange;

@SuppressWarnings("rawtypes")
@AutoService({ChangeRule.class})
public class IllegalChangeTypesRule extends AbstractLintRule implements ChangeRule<Change> {
    private static final String NAME = "illegal-change-types";
    private static final String MESSAGE = "Change type '%s' is not allowed in this project";

    public IllegalChangeTypesRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<Change> getChangeType() {
        return Change.class;
    }

    @Override
    public boolean invalid(Change change) {
        if (ruleConfig.getValues() != null) {
            return ruleConfig.getValues().stream()
                .anyMatch(illegal -> getChangeName(change).equals(illegal) || getChangeClassName(change).equals(illegal));
        }
        return false;
    }

    private String getChangeClassName(Change change) {
        return change.getClass().getName();
    }

    private String getChangeName(Change change) {
        return change.getClass().getAnnotation(DatabaseChange.class).name();
    }

    @Override
    public String getMessage(Change change) {
        return formatMessage(change.getClass().getCanonicalName());
    }
}
