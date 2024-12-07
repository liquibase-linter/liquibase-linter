package io.github.liquibaselinter.rules;

import java.util.StringJoiner;

public class RuleViolation {

    private final String message;

    public RuleViolation(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", RuleViolation.class.getSimpleName() + "[", "]")
            .add("message='" + message + "'")
            .toString();
    }
}
