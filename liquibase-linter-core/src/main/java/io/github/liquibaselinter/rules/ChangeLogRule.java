package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.Collection;
import liquibase.changelog.DatabaseChangeLog;

public interface ChangeLogRule {
    String getName();

    Collection<RuleViolation> check(DatabaseChangeLog changeLog, RuleConfig ruleConfig);
}
