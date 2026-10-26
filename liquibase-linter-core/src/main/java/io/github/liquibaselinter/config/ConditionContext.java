package io.github.liquibaselinter.config;

import java.util.Optional;
import liquibase.Contexts;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;

public record ConditionContext(DatabaseChangeLog changeLog, ChangeSet changeSet, Change change) {
    public static ConditionContext from(Change change) {
        return new ConditionContext(change.getChangeSet().getChangeLog(), change.getChangeSet(), change);
    }

    public static ConditionContext from(ChangeSet changeSet) {
        return new ConditionContext(changeSet.getChangeLog(), changeSet, null);
    }

    public static ConditionContext from(DatabaseChangeLog changeLog) {
        return new ConditionContext(changeLog, null, null);
    }

    public boolean matchesContext(String... toMatch) {
        return Optional.ofNullable(changeSet)
            .map(ChangeSet::getContextFilter)
            .map(contexts -> !contexts.isEmpty() && contexts.matches(new Contexts(toMatch)))
            .orElse(false);
    }
}
