package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeSetRule;
import liquibase.changelog.ChangeSet;

@AutoService(ChangeSetRule.class)
public class ChangetSetIdRule extends AbstractLintRule implements ChangeSetRule {
    private static final String NAME = "changeset-id";
    private static final String MESSAGE = "ChangeSet id '%s' does not follow pattern '%s'";

    public ChangetSetIdRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        return checkMandatoryPattern(changeSet.getId(), changeSet);
    }

    @Override
    public String getMessage(ChangeSet changeSet) {
        return formatMessage(changeSet.getId(), ruleConfig.getPatternString());
    }
}
