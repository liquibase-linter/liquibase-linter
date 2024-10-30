package io.github.liquibaselinter.config.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.rules.AbstractLintRule;
import io.github.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.AbstractChange;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.RenameTableChange;

@SuppressWarnings("rawtypes")
@AutoService({ChangeRule.class})
public class TableNameLengthRule extends AbstractLintRule implements ChangeRule<AbstractChange> {
    private static final String NAME = "table-name-length";
    private static final String MESSAGE = "Table '%s' name must not be longer than %d";

    public TableNameLengthRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<AbstractChange> getChangeType() {
        return AbstractChange.class;
    }

    @Override
    public boolean supports(AbstractChange change) {
        return change instanceof CreateTableChange || change instanceof RenameTableChange;
    }

    @Override
    public boolean invalid(AbstractChange change) {
        String tableName = getTableName(change);
        return tableName != null && checkMaxLength(tableName);
    }

    @Override
    public String getMessage(AbstractChange change) {
        return formatMessage(getTableName(change), getConfig().getMaxLength());
    }

    private String getTableName(AbstractChange change) {
        if (change instanceof CreateTableChange) {
            return ((CreateTableChange) change).getTableName();
        } else {
            return ((RenameTableChange) change).getNewTableName();
        }
    }
}
