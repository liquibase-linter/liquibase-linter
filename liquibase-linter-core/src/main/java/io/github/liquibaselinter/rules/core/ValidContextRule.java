package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeSetRule;
import liquibase.ContextExpression;
import liquibase.changelog.ChangeSet;

@AutoService({ChangeSetRule.class})
public class ValidContextRule extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "valid-context";
    private static final String MESSAGE = "Context does not follow pattern";

    public ValidContextRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        ContextExpression contextExpression = changeSet.getContexts();
        if (contextExpression != null) {
            for (String context : contextExpression.getContexts()) {
                if (checkPattern(context, changeSet)) {
                    return true;
                }
            }
        }
        return false;
    }
}
