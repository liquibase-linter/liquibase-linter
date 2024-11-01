package io.github.liquibaselinter.rules;

import liquibase.changelog.DatabaseChangeLog;

public interface ChangeLogRule extends LintRule {
    boolean invalid(DatabaseChangeLog changeLog);

    default String getMessage(DatabaseChangeLog changeLog) {
        return getMessage();
    }
}
