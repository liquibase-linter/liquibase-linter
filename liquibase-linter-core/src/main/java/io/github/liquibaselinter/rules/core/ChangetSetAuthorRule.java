package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeSetRule;
import liquibase.changelog.ChangeSet;

@AutoService(ChangeSetRule.class)
public class ChangetSetAuthorRule extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "changeset-author";
    private static final String MESSAGE = "ChangeSet author '%s' does not follow pattern '%s'";

    public ChangetSetAuthorRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        return checkMandatoryPattern(changeSet.getAuthor(), changeSet);
    }

    @Override
    public String getMessage(ChangeSet changeSet) {
        return formatMessage(changeSet.getAuthor(), ruleConfig.getPatternString());
    }
}
