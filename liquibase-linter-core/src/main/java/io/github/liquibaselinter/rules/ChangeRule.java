package io.github.liquibaselinter.rules;

import liquibase.change.Change;

public interface ChangeRule extends LintRule {

    boolean supports(Change change);

    boolean invalid(Change change);

    default String getMessage(Change change) {
        return getMessage();
    }
}
