package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeSetRule;
import liquibase.changelog.ChangeSet;

@SuppressWarnings("rawtypes")
@AutoService({ChangeSetRule.class})
public class HasContextRule extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "has-context";
    private static final String MESSAGE = "Should have at least one context on the change set";

    public HasContextRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        return changeSet.getContexts().isEmpty();
    }
}
