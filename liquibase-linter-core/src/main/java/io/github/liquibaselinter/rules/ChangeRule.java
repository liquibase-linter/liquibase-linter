package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.Collection;
import liquibase.change.Change;

public interface ChangeRule {
    String getName();

    Collection<RuleViolation> check(Change change, RuleConfig ruleConfig);
}
