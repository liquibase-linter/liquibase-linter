package io.github.liquibaselinter.rules;

import liquibase.change.Change;

public interface ChangeRule<T extends Change> extends LintRule {

    boolean supports(Change change);

    boolean invalid(T change);

    default String getMessage(T change) {
        return getMessage();
    }
}
