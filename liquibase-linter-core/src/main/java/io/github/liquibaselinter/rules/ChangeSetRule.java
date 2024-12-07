package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.changelog.ChangeSet;

import java.util.Collection;

public interface ChangeSetRule extends LintRule {
    Collection<RuleViolation> check(ChangeSet changeSet, RuleConfig ruleConfig);
}
