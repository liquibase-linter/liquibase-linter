package io.github.liquibaselinter;

import io.github.liquibaselinter.config.Config;
import io.github.liquibaselinter.report.Report;
import liquibase.ContextExpression;
import liquibase.change.Change;
import liquibase.change.core.*;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ChangeLogLinter {

    public static final List<Class<? extends Change>> DDL_CHANGE_TYPES = Collections.unmodifiableList(
        Arrays.asList(
            DropViewChange.class,
            AddUniqueConstraintChange.class,
            DropColumnChange.class,
            DropIndexChange.class,
            AddForeignKeyConstraintChange.class,
            ModifyDataTypeChange.class,
            DropNotNullConstraintChange.class,
            RenameTableChange.class,
            MergeColumnChange.class,
            AlterSequenceChange.class,
            CreateIndexChange.class,
            RenameViewChange.class,
            DropPrimaryKeyChange.class,
            DropUniqueConstraintChange.class,
            DropSequenceChange.class,
            RenameSequenceChange.class,
            CreateSequenceChange.class,
            AddNotNullConstraintChange.class,
            DropDefaultValueChange.class,
            AddColumnChange.class,
            DropTableChange.class,
            DropAllForeignKeyConstraintsChange.class,
            CreateViewChange.class,
            CreateTableChange.class,
            RenameColumnChange.class,
            CreateProcedureChange.class,
            DropForeignKeyConstraintChange.class,
            DropProcedureChange.class,
            AddPrimaryKeyChange.class,
            AddDefaultValueChange.class
        )
    );
    public static final List<Class<? extends Change>> DML_CHANGE_TYPES = Collections.unmodifiableList(
        Arrays.asList(
            InsertDataChange.class,
            UpdateDataChange.class,
            DeleteDataChange.class,
            LoadDataChange.class,
            LoadUpdateDataChange.class
        )
    );

    private final Config config;
    private final RuleRunner ruleRunner;

    public ChangeLogLinter(Config config) {
        this.config = config;
        this.ruleRunner = new RuleRunner(config);
    }

    public void lintChangeLog(final DatabaseChangeLog databaseChangeLog) throws ChangeLogLintingException {

        if (shouldLint(databaseChangeLog)) {
            ruleRunner.checkChangeLog(databaseChangeLog);
        }

        lintChangeSets(databaseChangeLog.getChangeSets());

        ruleRunner.getFilesParsed().add(databaseChangeLog.getPhysicalFilePath());
    }

    private void lintChangeSets(List<ChangeSet> changeSets) throws ChangeLogLintingException {
        for (ChangeSet changeSet : changeSets) {

            DatabaseChangeLog databaseChangeLog = changeSet.getChangeLog();
            if (shouldLint(databaseChangeLog) && isNotRootChangeLog(databaseChangeLog)) {
                ruleRunner.checkChangeLog(databaseChangeLog);
            }
            ruleRunner.checkDuplicateIncludes(databaseChangeLog);

            if (shouldLint(changeSet)) {
                ruleRunner.checkChangeSet(changeSet);

                for (Change change : changeSet.getChanges()) {
                    ruleRunner.checkChange(change);
                }
            }

            ruleRunner.getFilesParsed().add(databaseChangeLog.getPhysicalFilePath());
        }
    }

    private static boolean isNotRootChangeLog(DatabaseChangeLog databaseChangeLog) {
        return databaseChangeLog.getRootChangeLog() != databaseChangeLog;
    }

    private boolean shouldLint(DatabaseChangeLog changeLog) {
        return isEnabled()
            && isFilePathNotIgnored(changeLog.getFilePath())
            && !hasAlreadyBeenParsed(changeLog.getFilePath());
    }

    private boolean isEnabled() {
        return StringUtils.isEmpty(config.getEnableAfter()) || hasAlreadyBeenParsed(config.getEnableAfter());
    }

    private boolean hasAlreadyBeenParsed(String filePath) {
        return ruleRunner.getFilesParsed().contains(filePath);
    }

    private boolean shouldLint(ChangeSet changeSet) {
        return isEnabled()
            && !isContextIgnored(changeSet)
            && isFilePathNotIgnored(changeSet.getFilePath());
    }

    private boolean isContextIgnored(ChangeSet changeSet) {
        final Set<String> contexts = Optional.ofNullable(changeSet.getContexts())
            .map(ContextExpression::getContexts).orElseGet(Collections::emptySet);
        if (config.getIgnoreContextPattern() != null && !contexts.isEmpty()) {
            return contexts.stream().anyMatch(context -> config.getIgnoreContextPattern().matcher(context).matches());
        }
        return false;
    }

    private boolean isFilePathNotIgnored(String filePath) {
        if (filePath != null && config.getIgnoreFilesPattern() != null) {
            String changeLogPath = filePath.replace('\\', '/');
            return !config.getIgnoreFilesPattern().matcher(changeLogPath).matches();
        }
        return true;
    }

    public Set<String> getFilesParsed() {
        return ruleRunner.getFilesParsed();
    }

    public Report report() {
        return ruleRunner.buildReport();
    }
}
