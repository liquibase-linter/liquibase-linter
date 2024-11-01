package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeSetRule;
import io.github.liquibaselinter.rules.Changes;
import liquibase.changelog.ChangeSet;

@SuppressWarnings("rawtypes")
@AutoService({ChangeSetRule.class})
public class IsolateDDLChangesRule extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "isolate-ddl-changes";
    private static final String MESSAGE = "Should only have a single ddl change per change set";

    public IsolateDDLChangesRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        return changeSet.getChanges().stream().filter(Changes::isDDL).count() > 1;
    }
}
