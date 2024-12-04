package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.core.CreateIndexChange;

@AutoService(ChangeRule.class)
public class IndexNameRule extends AbstractLintRule implements ChangeRule {
    private static final String NAME = "index-name";
    private static final String MESSAGE = "Index name does not follow pattern";

    public IndexNameRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean supports(Change change) {
        return change instanceof CreateIndexChange;
    }

    @Override
    public boolean invalid(Change change) {
        CreateIndexChange createIndexChange = (CreateIndexChange) change;
        return checkMandatoryPattern(createIndexChange.getIndexName(), change);
    }

    @Override
    public String getMessage(Change change) {
        CreateIndexChange createIndexChange = (CreateIndexChange) change;
        return formatMessage(createIndexChange.getIndexName());
    }

}
