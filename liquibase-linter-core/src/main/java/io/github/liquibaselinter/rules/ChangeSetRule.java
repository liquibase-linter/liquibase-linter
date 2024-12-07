package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.changelog.ChangeSet;

public interface ChangeSetRule extends LintRule {

    String getMessage();

    boolean invalid(ChangeSet changeSet);

    void configure(RuleConfig ruleConfig);

    default String getMessage(ChangeSet changeSet) {
        return getMessage();
    }
}
