package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.Collection;
import liquibase.changelog.ChangeSet;

public interface ChangeSetRule extends LintRule {
    Collection<RuleViolation> check(ChangeSet changeSet, RuleConfig ruleConfig);
}
