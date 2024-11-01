package io.github.liquibaselinter;

import io.github.liquibaselinter.config.Config;
import io.github.liquibaselinter.rules.RuleRunner;
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

    public void lintChangeLog(final DatabaseChangeLog databaseChangeLog, Config config, RuleRunner ruleRunner) throws ChangeLogLintingException {
        if (shouldLint(databaseChangeLog, config, ruleRunner)) {
            ruleRunner.checkChangeLog(databaseChangeLog);
        }
        lintChangeSets(databaseChangeLog, config, ruleRunner);
    }

    private void lintChangeSets(DatabaseChangeLog databaseChangeLog, Config config, RuleRunner ruleRunner) throws ChangeLogLintingException {
        final List<ChangeSet> changeSets = databaseChangeLog.getChangeSets();

        for (ChangeSet changeSet : changeSets) {
            if (shouldLint(changeSet, config, ruleRunner)) {
                ruleRunner.checkChangeSet(changeSet);

                for (Change change : changeSet.getChanges()) {
                    ruleRunner.checkChange(change);
                }
            }
        }
    }

    private boolean shouldLint(DatabaseChangeLog changeLog, Config config, RuleRunner ruleRunner) {
        return isEnabled(config, ruleRunner)
            && isFilePathNotIgnored(changeLog.getFilePath(), config)
            && !hasAlreadyBeenParsed(changeLog.getFilePath(), ruleRunner);
    }

    private boolean isEnabled(Config config, RuleRunner ruleRunner) {
        return StringUtils.isEmpty(config.getEnableAfter()) || hasAlreadyBeenParsed(config.getEnableAfter(), ruleRunner);
    }

    private boolean hasAlreadyBeenParsed(String filePath, RuleRunner ruleRunner) {
        return ruleRunner.getFilesParsed().contains(filePath);
    }

    private boolean shouldLint(ChangeSet changeSet, Config config, RuleRunner ruleRunner) {
        return isEnabled(config, ruleRunner)
            && !isContextIgnored(changeSet, config)
            && isFilePathNotIgnored(changeSet.getFilePath(), config)
            && !hasAlreadyBeenParsed(changeSet.getFilePath(), ruleRunner);
    }

    private boolean isContextIgnored(ChangeSet changeSet, Config config) {
        final Set<String> contexts = Optional.ofNullable(changeSet.getContexts())
            .map(ContextExpression::getContexts).orElseGet(Collections::emptySet);
        if (config.getIgnoreContextPattern() != null && !contexts.isEmpty()) {
            return contexts.stream().anyMatch(context -> config.getIgnoreContextPattern().matcher(context).matches());
        }
        return false;
    }

    private boolean isFilePathNotIgnored(String filePath, Config config) {
        if (filePath != null && config.getIgnoreFilesPattern() != null) {
            String changeLogPath = filePath.replace('\\', '/');
            return !config.getIgnoreFilesPattern().matcher(changeLogPath).matches();
        }
        return true;
    }

}
