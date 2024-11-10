package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.core.CreateIndexChange;

@SuppressWarnings("rawtypes")
@AutoService(ChangeRule.class)
public class IndexTablespaceRule extends AbstractLintRule implements ChangeRule<CreateIndexChange> {
    private static final String NAME = "index-tablespace";
    private static final String MESSAGE = "Tablespace '%s' of index '%s' is empty or does not follow pattern '%s'";

    public IndexTablespaceRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<CreateIndexChange> getChangeType() {
        return CreateIndexChange.class;
    }

    @Override
    public boolean invalid(CreateIndexChange change) {
        return checkMandatoryPattern(change.getTablespace(), change);
    }

    @Override
    public String getMessage(CreateIndexChange change) {
        return formatMessage(change.getTablespace(), change.getIndexName(), getPatternForMessage(change));
    }

}
