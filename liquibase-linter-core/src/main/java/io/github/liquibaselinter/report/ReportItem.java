package io.github.liquibaselinter.report;

import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;

public record ReportItem(String filePath, String changeSetId, String rule, ReportItemType type, String message) {
    public static ReportItem error(
        DatabaseChangeLog databaseChangeLog,
        ChangeSet changeSet,
        String rule,
        String message
    ) {
        return create(databaseChangeLog, changeSet, rule, message, ReportItemType.ERROR);
    }

    public static ReportItem ignored(
        DatabaseChangeLog databaseChangeLog,
        ChangeSet changeSet,
        String rule,
        String message
    ) {
        return create(databaseChangeLog, changeSet, rule, message, ReportItemType.IGNORED);
    }

    public static ReportItem passed(
        DatabaseChangeLog databaseChangeLog,
        ChangeSet changeSet,
        String rule,
        String message
    ) {
        return create(databaseChangeLog, changeSet, rule, message, ReportItemType.PASSED);
    }

    private static ReportItem create(
        DatabaseChangeLog databaseChangeLog,
        ChangeSet changeSet,
        String rule,
        String message,
        ReportItemType type
    ) {
        return new ReportItem(
            getFilePath(databaseChangeLog, changeSet),
            changeSet == null ? null : changeSet.getId(),
            rule,
            type,
            message
        );
    }

    private static String getFilePath(DatabaseChangeLog databaseChangeLog, ChangeSet changeSet) {
        if (changeSet != null) {
            return changeSet.getFilePath();
        } else if (databaseChangeLog != null) {
            return databaseChangeLog.getFilePath();
        }
        return null;
    }

    public enum ReportItemType {
        ERROR,
        IGNORED,
        PASSED,
    }
}
