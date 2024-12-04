package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.Change;

public interface ChangeRule extends LintRule {

    void configure(RuleConfig ruleConfig);

    String getMessage();

    boolean supports(Change change);

    boolean invalid(Change change);

    default String getMessage(Change change) {
        return getMessage();
    }
}
