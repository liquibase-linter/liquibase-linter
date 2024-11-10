package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;

public interface LintRule {
    String getName();

    void configure(RuleConfig ruleConfig);

    String getMessage();
}
