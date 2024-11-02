package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeSetRule;
import liquibase.change.core.TagDatabaseChange;
import liquibase.changelog.ChangeSet;

@SuppressWarnings("rawtypes")
@AutoService({ChangeSetRule.class})
public class HasCommentRule extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "has-comment";
    private static final String MESSAGE = "Change set must have a comment";

    public HasCommentRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        if (changeSet.getChanges().stream().anyMatch(change -> change instanceof TagDatabaseChange)) {
            /*
            https://github.com/whiteclarkegroup/liquibase-linter/issues/90
            tagDatabase changes cannot have any siblings in a changeSet - not even comments
            so we have to make an exception here
             */
            return false;
        }
        return checkNotBlank(changeSet.getComments());
    }
}
