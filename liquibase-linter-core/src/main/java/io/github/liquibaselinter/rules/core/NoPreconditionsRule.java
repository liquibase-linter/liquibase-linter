package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.*;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.precondition.core.PreconditionContainer;

import java.util.Collection;
import java.util.Collections;

@AutoService({ChangeLogRule.class, ChangeSetRule.class})
public class NoPreconditionsRule extends AbstractLintRule implements ChangeSetRule, ChangeLogRule {
    private static final String NAME = "no-preconditions";
    private static final String MESSAGE = "Preconditions are not allowed in this project";

    public NoPreconditionsRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public Collection<RuleViolation> check(DatabaseChangeLog changeLog, RuleConfig ruleConfig) {
        LintRuleMessageGenerator messageGenerator = new LintRuleMessageGenerator(MESSAGE, ruleConfig);
        if (isInvalid(changeLog)) {
            return Collections.singleton(new RuleViolation(messageGenerator.getMessage()));
        }
        return Collections.emptyList();
    }

    @Override
    public boolean invalid(ChangeSet changeSet) {
        return changeSet.getPreconditions() != null && !changeSet.getPreconditions().getNestedPreconditions().isEmpty();
    }

    public boolean isInvalid(DatabaseChangeLog changeLog) {
        return isInvalid(changeLog.getPreconditions());
    }

    private static boolean isInvalid(PreconditionContainer preconditions) {
        if (preconditions == null || preconditions.getNestedPreconditions().isEmpty()) {
            return false;
        }
        if (preconditions.getNestedPreconditions().stream().anyMatch(precondition -> !(precondition instanceof PreconditionContainer))) {
            return true;
        }
        return preconditions.getNestedPreconditions().stream()
            .map(PreconditionContainer.class::cast)
            .anyMatch(NoPreconditionsRule::isInvalid);
    }
}
