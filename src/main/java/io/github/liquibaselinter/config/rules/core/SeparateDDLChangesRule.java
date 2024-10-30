package io.github.liquibaselinter.config.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.ChangeLogLinter;
import io.github.liquibaselinter.config.rules.AbstractLintRule;
import io.github.liquibaselinter.config.rules.ChangeSetRule;
import liquibase.ContextExpression;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;

import java.util.Collection;

@AutoService({ChangeSetRule.class})
public class SeparateDDLChangesRule extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "separate-ddl-context";
    private static final String MESSAGE = "Should have a ddl changes under ddl contexts";

    public SeparateDDLChangesRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        ContextExpression contextExpression = changeSet.getContexts();
        if (contextExpression != null && !contextExpression.getContexts().isEmpty()) {
            Collection<String> contexts = contextExpression.getContexts();
            for (Change change : changeSet.getChanges()) {
                if (ChangeLogLinter.DDL_CHANGE_TYPES.contains(change.getClass())) {
                    for (String context : contexts) {
                        if (!getConfig().getPattern().map(pattern -> pattern.matcher(context).matches()).orElse(true)) {
                            return true;
                        }
                    }
                } else if (ChangeLogLinter.DML_CHANGE_TYPES.contains(change.getClass())) {
                    for (String context : contexts) {
                        if (getConfig().getPattern().map(pattern -> pattern.matcher(context).matches()).orElse(false)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
