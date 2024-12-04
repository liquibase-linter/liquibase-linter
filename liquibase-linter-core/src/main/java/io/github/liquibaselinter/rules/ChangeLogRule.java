package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.changelog.DatabaseChangeLog;

import java.util.Collection;

public interface ChangeLogRule extends LintRule {
    Collection<RuleViolation> check(DatabaseChangeLog changeLog, RuleConfig ruleConfig);
}
