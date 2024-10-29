package io.github.liquibaselinter.config.rules;

public interface LintRule {
    String getName();

    void configure(RuleConfig ruleConfig);

    RuleConfig getConfig();

    String getMessage();
}
