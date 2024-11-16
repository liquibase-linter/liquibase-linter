package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.core.*;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class ObjectNameRules {

    private static boolean doesSupport(Change change) {
        return !getObjectNames(change).isEmpty();
    }

    private static Collection<String> getObjectNames(Change change) {
        if (change instanceof AddColumnChange) {
            return ((AddColumnChange) change).getColumns().stream().map(ColumnConfig::getName).collect(Collectors.toList());
        } else if (change instanceof AddForeignKeyConstraintChange) {
            return Collections.singletonList(((AddForeignKeyConstraintChange) change).getConstraintName());
        } else if (change instanceof AddPrimaryKeyChange) {
            return Collections.singletonList(((AddPrimaryKeyChange) change).getConstraintName());
        } else if (change instanceof AddUniqueConstraintChange) {
            return Collections.singletonList(((AddUniqueConstraintChange) change).getConstraintName());
        } else if (change instanceof CreateTableChange) {
            return ((CreateTableChange) change).getColumns().stream().map(ColumnConfig::getName).collect(Collectors.toList());
        } else if (change instanceof MergeColumnChange) {
            return Collections.singletonList(((MergeColumnChange) change).getFinalColumnName());
        } else if (change instanceof RenameColumnChange) {
            return Collections.singletonList(((RenameColumnChange) change).getNewColumnName());
        } else if (change instanceof RenameViewChange) {
            return Collections.singletonList(((RenameViewChange) change).getNewViewName());
        } else if (change instanceof CreateViewChange) {
            return Collections.singletonList(((CreateViewChange) change).getViewName());
        } else if (change instanceof CreateIndexChange) {
            return Collections.singletonList(((CreateIndexChange) change).getIndexName());
        }
        return Collections.emptyList();
    }

    @SuppressWarnings("rawtypes")
    @AutoService(ChangeRule.class)
    public static class ObjectNameRule extends AbstractLintRule implements ChangeRule<Change> {
        private static final String NAME = "object-name";
        private static final String MESSAGE = "Object name does not follow pattern";

        public ObjectNameRule() {
            super(NAME, MESSAGE);
        }

        @Override
        public Class<Change> getChangeType() {
            return Change.class;
        }

        @Override
        public boolean supports(Change change) {
            return doesSupport(change);
        }

        @Override
        public boolean invalid(Change change) {
            return getObjectNames(change).stream().anyMatch(objectName -> checkMandatoryPattern(objectName, change));
        }

        @Override
        public String getMessage(Change change) {
            String joined = getObjectNames(change).stream().filter(objectName -> checkMandatoryPattern(objectName, change)).collect(Collectors.joining(","));
            return formatMessage(joined, getConfig().getPatternString());
        }

    }

    @SuppressWarnings("rawtypes")
    @AutoService(ChangeRule.class)
    public static class ObjectNameLengthRule extends AbstractLintRule implements ChangeRule<Change> {
        private static final String NAME = "object-name-length";
        private static final String MESSAGE = "Object name '%s' must be less than %d characters";

        public ObjectNameLengthRule() {
            super(NAME, MESSAGE);
        }

        @Override
        public Class<Change> getChangeType() {
            return Change.class;
        }

        @Override
        public boolean supports(Change change) {
            return doesSupport(change);
        }

        @Override
        public boolean invalid(Change change) {
            return getObjectNames(change).stream().anyMatch(this::checkMaxLength);
        }

        @Override
        public String getMessage(Change change) {
            String joined = getObjectNames(change).stream().filter(this::checkMaxLength).collect(Collectors.joining(","));
            return formatMessage(joined, getConfig().getMaxLength());

        }
    }
}
