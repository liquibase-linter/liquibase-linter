package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeLogRule;
import io.github.liquibaselinter.rules.ChangeSetRule;
import io.github.liquibaselinter.rules.LintRuleMessageGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.precondition.core.PreconditionContainer;

@AutoService({ ChangeLogRule.class, ChangeSetRule.class })
public class NoPreconditionsRule implements ChangeSetRule, ChangeLogRule {

    private static final String NAME = "no-preconditions";
    private static final String DEFAULT_MESSAGE = "Preconditions are not allowed in this project";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(DatabaseChangeLog changeLog, RuleConfig ruleConfig) {
        if (isInvalid(changeLog)) {
            LintRuleMessageGenerator messageGenerator = new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig);
            return Collections.singleton(new RuleViolation(messageGenerator.getMessage()));
        }
        return Collections.emptyList();
    }

    @Override
    public Collection<RuleViolation> check(ChangeSet changeSet, RuleConfig ruleConfig) {
        if (changeSet.getPreconditions() != null && !changeSet.getPreconditions().getNestedPreconditions().isEmpty()) {
            LintRuleMessageGenerator messageGenerator = new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig);
            return Collections.singleton(new RuleViolation(messageGenerator.getMessage()));
        }
        return Collections.emptyList();
    }

    public boolean isInvalid(DatabaseChangeLog changeLog) {
        return isInvalid(changeLog.getPreconditions());
    }

    private static boolean isInvalid(PreconditionContainer preconditions) {
        if (preconditions == null || preconditions.getNestedPreconditions().isEmpty()) {
            return false;
        }
        if (
            preconditions
                .getNestedPreconditions()
                .stream()
                .anyMatch(precondition -> !(precondition instanceof PreconditionContainer))
        ) {
            return true;
        }
        return preconditions
            .getNestedPreconditions()
            .stream()
            .map(PreconditionContainer.class::cast)
            .anyMatch(NoPreconditionsRule::isInvalid);
    }
}
