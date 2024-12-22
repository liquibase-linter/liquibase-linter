package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.Collection;
import liquibase.change.Change;

public interface ChangeRule extends LintRule {
    Collection<RuleViolation> check(Change change, RuleConfig ruleConfig);

    @Deprecated
    void configure(RuleConfig ruleConfig);

    @Deprecated
    boolean invalid(Change change);
}
