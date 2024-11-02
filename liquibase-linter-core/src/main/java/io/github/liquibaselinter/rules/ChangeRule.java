package io.github.liquibaselinter.rules;

import liquibase.change.Change;

public interface ChangeRule<T extends Change> extends LintRule {
    Class<T> getChangeType();

    default boolean supports(T change) {
        return true;
    }

    boolean invalid(T change);

    default String getMessage(T change) {
        return getMessage();
    }
}
