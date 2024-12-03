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
public class PrimaryKeyNameRule extends AbstractLintRule implements ChangeRule {
    private static final String NAME = "primary-key-name";
    private static final String MESSAGE = "Primary key name '%s' is missing or does not follow pattern '%s'";

    public PrimaryKeyNameRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean supports(Change change) {
        if (change.getClass().isAssignableFrom(AddPrimaryKeyChange.class)) {
            return true;
        }
        if (change.getClass().isAssignableFrom(CreateTableChange.class)) {
            return !primaryKeyNamesFromCreateTable((CreateTableChange)change).isEmpty();
        }
        return false;
    }

    private static List<String> primaryKeyNamesFromCreateTable(CreateTableChange change) {
        if (change.getColumns() == null) {
            return Collections.emptyList();
        }
        return change.getColumns()
            .stream()
            .map(ColumnConfig::getConstraints)
            .filter(Objects::nonNull)
            .filter(constraint -> Boolean.TRUE.equals(constraint.isPrimaryKey()) || constraint.getPrimaryKeyName() != null)
            .map(ConstraintsConfig::getPrimaryKeyName)
            .distinct()
            .collect(Collectors.toList());
    }

    @Override
    public boolean invalid(Change change) {
        return extractConstraintNamesFrom(change).stream().anyMatch(constraintName -> checkMandatoryPattern(constraintName, change));
    }

    private Collection<String> extractConstraintNamesFrom(Change change) {
        if (change instanceof AddPrimaryKeyChange) {
            return Collections.singleton(((AddPrimaryKeyChange) change).getConstraintName());
        }
        if (change instanceof CreateTableChange) {
            return primaryKeyNamesFromCreateTable((CreateTableChange) change);
        }
        throw new IllegalStateException("Can't retrieve constraint names from " + change.getClass());
    }

    @Override
    public String getMessage(Change change) {
        String invalidPrimaryKeys = extractConstraintNamesFrom(change)
            .stream()
            .filter(constraintName -> checkMandatoryPattern(constraintName, change))
            .map(constraintName -> constraintName == null ? "" : constraintName)
            .collect(Collectors.joining(","));
        return formatMessage(invalidPrimaryKeys, getPatternForMessage(change));
    }
}
