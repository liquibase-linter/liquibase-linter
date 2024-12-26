package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.Collection;
import liquibase.changelog.ChangeSet;

public interface ChangeSetRule {
    String getName();

    Collection<RuleViolation> check(ChangeSet changeSet, RuleConfig ruleConfig);
}
