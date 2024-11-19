package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.RenameTableChange;

@SuppressWarnings("rawtypes")
@AutoService(ChangeRule.class)
public class TableNameLengthRule extends AbstractLintRule implements ChangeRule<Change> {
    private static final String NAME = "table-name-length";
    private static final String MESSAGE = "Table '%s' name must not be longer than %d";

    public TableNameLengthRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean supports(Change change) {
        return change instanceof CreateTableChange || change instanceof RenameTableChange;
    }

    @Override
    public boolean invalid(Change change) {
        String tableName = getTableName(change);
        return tableName != null && checkMaxLength(tableName);
    }

    @Override
    public String getMessage(Change change) {
        return formatMessage(getTableName(change), getConfig().getMaxLength());
    }

    private String getTableName(Change change) {
        if (change instanceof CreateTableChange) {
            return ((CreateTableChange) change).getTableName();
        } else {
            return ((RenameTableChange) change).getNewTableName();
        }
    }
}
