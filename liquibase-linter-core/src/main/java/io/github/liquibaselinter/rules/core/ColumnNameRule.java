package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.MergeColumnChange;
import liquibase.change.core.RenameColumnChange;

@SuppressWarnings("rawtypes")
@AutoService(ChangeRule.class)
public class ColumnNameRule extends AbstractLintRule implements ChangeRule<Change> {
    private static final String NAME = "column-name";
    private static final String MESSAGE = "Column name '%s' does not follow pattern '%s'";

    public ColumnNameRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<Change> getChangeType() {
        return Change.class;
    }

    @Override
    public boolean supports(Change change) {
        return change instanceof AddColumnChange
            || change instanceof RenameColumnChange
            || change instanceof CreateTableChange
            || change instanceof MergeColumnChange;
    }

    @Override
    public boolean invalid(Change change) {
        return getColumnNames(change).stream().anyMatch(columnName -> checkMandatoryPattern(columnName, change));
    }

    @Override
    public String getMessage(Change change) {
        String invalidColumnNames = getColumnNames(change)
            .stream()
            .filter(columnName -> checkMandatoryPattern(columnName, change))
            .map(columnName -> columnName == null ? "" : columnName)
            .collect(Collectors.joining(","));
        return formatMessage(invalidColumnNames, getConfig().getPatternString());
    }

    private Set<String> getColumnNames(Change change) {
        if (change instanceof AddColumnChange) {
            return ((AddColumnChange) change).getColumns().stream().map(ColumnConfig::getName).collect(Collectors.toSet());
        } else if (change instanceof CreateTableChange) {
            return ((CreateTableChange) change).getColumns().stream().map(ColumnConfig::getName).collect(Collectors.toSet());
        } else if (change instanceof RenameColumnChange) {
            return Collections.singleton(((RenameColumnChange) change).getNewColumnName());
        } else if (change instanceof MergeColumnChange) {
            return Collections.singleton(((MergeColumnChange) change).getFinalColumnName());
        }
        return new HashSet<>();
    }
}
