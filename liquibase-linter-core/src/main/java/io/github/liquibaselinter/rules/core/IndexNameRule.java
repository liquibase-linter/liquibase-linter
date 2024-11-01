package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.core.CreateIndexChange;

@SuppressWarnings("rawtypes")
@AutoService({ChangeRule.class})
public class IndexNameRule extends AbstractLintRule implements ChangeRule<CreateIndexChange> {
    private static final String NAME = "index-name";
    private static final String MESSAGE = "Index name does not follow pattern";

    public IndexNameRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<CreateIndexChange> getChangeType() {
        return CreateIndexChange.class;
    }

    @Override
    public boolean invalid(CreateIndexChange change) {
        return checkMandatoryPattern(change.getIndexName(), change);
    }

    @Override
    public String getMessage(CreateIndexChange change) {
        return formatMessage(change.getIndexName());
    }

}
