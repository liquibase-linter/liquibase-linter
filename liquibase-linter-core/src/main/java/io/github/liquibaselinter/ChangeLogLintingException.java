package io.github.liquibaselinter;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;

public class ChangeLogLintingException extends Exception {

    public ChangeLogLintingException(String message) {
        super(message);
    }

    public static ChangeLogLintingException from(
        DatabaseChangeLog databaseChangeLog,
        ChangeSet changeSet,
        String customMessage
    ) {
        if (changeSet != null) {
            final String message =
                "File name: " +
                changeSet.getFilePath() +
                " -- Change Set ID: " +
                changeSet.getId() +
                " -- Author: " +
                changeSet.getAuthor() +
                " -- Message: " +
                customMessage;
            return new ChangeLogLintingException(message);
        } else if (databaseChangeLog != null) {
            return from(databaseChangeLog, customMessage);
        }
        return new ChangeLogLintingException(customMessage);
    }

    private static ChangeLogLintingException from(DatabaseChangeLog databaseChangeLog, String customMessage) {
        final String message = "File name: " + databaseChangeLog.getFilePath() + " -- Message: " + customMessage;
        return new ChangeLogLintingException(message);
    }
}
