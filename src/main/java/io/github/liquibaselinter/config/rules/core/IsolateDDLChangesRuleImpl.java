package io.github.liquibaselinter.config.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.ChangeLogLinter;
import io.github.liquibaselinter.config.rules.AbstractLintRule;
import io.github.liquibaselinter.config.rules.ChangeSetRule;
import liquibase.changelog.ChangeSet;

@SuppressWarnings("rawtypes")
@AutoService({ChangeSetRule.class})
public class IsolateDDLChangesRuleImpl extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "isolate-ddl-changes";
    private static final String MESSAGE = "Should only have a single ddl change per change set";

    public IsolateDDLChangesRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        return changeSet.getChanges().stream().filter(cng -> ChangeLogLinter.DDL_CHANGE_TYPES.contains(cng.getClass())).count() > 1;
    }
}
