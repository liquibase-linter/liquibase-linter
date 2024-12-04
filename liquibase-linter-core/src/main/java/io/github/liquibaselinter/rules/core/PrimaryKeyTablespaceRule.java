package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.change.core.CreateTableChange;

@AutoService(ChangeRule.class)
public class PrimaryKeyTablespaceRule extends AbstractLintRule implements ChangeRule {
    private static final String NAME = "primary-key-tablespace";
    private static final String MESSAGE = "Tablespace '%s' is empty or does not follow pattern '%s'";

    public PrimaryKeyTablespaceRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean supports(Change change) {
        if (change.getClass().isAssignableFrom(AddPrimaryKeyChange.class)) {
            return true;
        }
        if (change.getClass().isAssignableFrom(CreateTableChange.class)) {
            return !primaryKeyTablespacesFromCreateTable((CreateTableChange)change).isEmpty();
        }
        return false;
    }

    private static List<String> primaryKeyTablespacesFromCreateTable(CreateTableChange change) {
        if (change.getColumns() == null) {
            return Collections.emptyList();
        }
        return change.getColumns()
            .stream()
            .map(ColumnConfig::getConstraints)
            .filter(Objects::nonNull)
            .filter(constraint -> Boolean.TRUE.equals(constraint.isPrimaryKey()) || constraint.getPrimaryKeyTablespace() != null)
            .map(ConstraintsConfig::getPrimaryKeyTablespace)
            .distinct()
            .collect(Collectors.toList());
    }

    @Override
    public boolean invalid(Change change) {
        return extractTablespacesFrom(change).stream().anyMatch(constraintName -> checkMandatoryPattern(constraintName, change));
    }

    private Collection<String> extractTablespacesFrom(Change change) {
        if (change instanceof AddPrimaryKeyChange) {
            return Collections.singleton(((AddPrimaryKeyChange) change).getTablespace());
        }
        if (change instanceof CreateTableChange) {
            return primaryKeyTablespacesFromCreateTable((CreateTableChange) change);
        }
        throw new IllegalStateException("Can't retrieve tablespace from " + change.getClass());
    }

    @Override
    public String getMessage(Change change) {
        String invalidTablespaces = extractTablespacesFrom(change)
            .stream()
            .filter(tablespace -> checkMandatoryPattern(tablespace, change))
            .map(tablespace -> tablespace == null ? "" : tablespace)
            .collect(Collectors.joining(","));
        return formatMessage(invalidTablespaces, getPatternForMessage(change));
    }
}
