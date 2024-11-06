package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.change.core.CreateTableChange;

@SuppressWarnings("rawtypes")
@AutoService({ChangeRule.class})
public class PrimaryKeyNameRule extends AbstractLintRule implements ChangeRule<Change> {
    private static final String NAME = "primary-key-name";
    private static final String MESSAGE = "Primary key name %s is missing or does not follow pattern '%s'";

    public PrimaryKeyNameRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<Change> getChangeType() {
        return Change.class;
    }

    @Override
    public boolean supports(Change change) {
        return change.getClass().isAssignableFrom(AddPrimaryKeyChange.class)
            || change.getClass().isAssignableFrom(CreateTableChange.class) && createTableContainsPrimaryKey((CreateTableChange)change);
    }

    private boolean createTableContainsPrimaryKey(CreateTableChange change) {
        return primaryKeyNamesFromCreateTable(change).anyMatch(Objects::nonNull);
    }

    private static Stream<String> primaryKeyNamesFromCreateTable(CreateTableChange change) {
        if (change.getColumns() == null) {
            return Stream.empty();
        }
        return change.getColumns()
            .stream()
            .map(ColumnConfig::getConstraints)
            .filter(Objects::nonNull)
            .map(ConstraintsConfig::getPrimaryKeyName)
            .distinct();
    }

    @Override
    public boolean invalid(Change change) {
        return extractConstraintNames(change).stream().anyMatch(constraintName -> checkMandatoryPattern(constraintName, change));
    }

    private Collection<String> extractConstraintNames(Change change) {
        if (change instanceof AddPrimaryKeyChange) {
            return Collections.singleton(((AddPrimaryKeyChange) change).getConstraintName());
        }
        if (change instanceof CreateTableChange) {
            CreateTableChange createTableChange = (CreateTableChange) change;
            return primaryKeyNamesFromCreateTable(createTableChange).collect(Collectors.toList());
        }
        throw new IllegalStateException("Can't retrieve constraint names from " + change.getClass());
    }

    @Override
    public String getMessage(Change change) {
        Collection<String> primaryKeys = extractConstraintNames(change)
            .stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        String pattern = ruleConfig.hasDynamicPattern() ? ruleConfig.getDynamicPattern(ruleConfig.getDynamicValue(change)).pattern() : ruleConfig.getPatternString();
        return formatMessage(String.join(",", primaryKeys), pattern);
    }
}
