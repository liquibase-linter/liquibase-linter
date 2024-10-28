package com.whiteclarkegroup.liquibaselinter.config.rules.core;

import com.google.auto.service.AutoService;
import com.whiteclarkegroup.liquibaselinter.config.rules.AbstractLintRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeLogRule;
import com.whiteclarkegroup.liquibaselinter.config.rules.ChangeSetRule;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.precondition.core.PreconditionContainer;

@AutoService({ChangeLogRule.class, ChangeSetRule.class})
public class NoPreconditionsRuleImpl extends AbstractLintRule implements ChangeSetRule, ChangeLogRule {
    private static final String NAME = "no-preconditions";
    private static final String MESSAGE = "Preconditions are not allowed in this project";

    public NoPreconditionsRuleImpl() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        return changeSet.getPreconditions() != null && !changeSet.getPreconditions().getNestedPreconditions().isEmpty();
    }

    @Override
    public boolean invalid(DatabaseChangeLog changeLog) {
        PreconditionContainer preconditions = changeLog.getPreconditions();
        return invalid(preconditions);
    }

    private static boolean invalid(PreconditionContainer preconditions) {
        if (preconditions == null || preconditions.getNestedPreconditions().isEmpty()) {
            return false;
        }
        if (preconditions.getNestedPreconditions().stream().anyMatch(precondition -> !(precondition instanceof PreconditionContainer))) {
            return true;
        }
        return preconditions.getNestedPreconditions().stream()
            .map(PreconditionContainer.class::cast)
            .anyMatch(NoPreconditionsRuleImpl::invalid);
    }
}
