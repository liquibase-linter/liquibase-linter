package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.core.CreateIndexChange;

@AutoService(ChangeRule.class)
public class IndexTablespaceRule extends AbstractLintRule implements ChangeRule {
    private static final String NAME = "index-tablespace";
    private static final String MESSAGE = "Tablespace '%s' of index '%s' is empty or does not follow pattern '%s'";

    public IndexTablespaceRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean supports(Change change) {
        return change instanceof CreateIndexChange;
    }

    @Override
    public boolean invalid(Change change) {
        CreateIndexChange createIndexChange = (CreateIndexChange) change;
        return checkMandatoryPattern(createIndexChange.getTablespace(), change);
    }

    @Override
    public String getMessage(Change change) {
        CreateIndexChange createIndexChange = (CreateIndexChange) change;
        return formatMessage(createIndexChange.getTablespace(), createIndexChange.getIndexName(), getPatternForMessage(change));
    }

}
