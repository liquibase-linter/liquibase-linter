package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.AbstractChange;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.RenameTableChange;

@SuppressWarnings("rawtypes")
@AutoService({ChangeRule.class})
public class TableNameRule extends AbstractLintRule implements ChangeRule<AbstractChange> {
    private static final String NAME = "table-name";
    private static final String MESSAGE = "Table name does not follow pattern";

    public TableNameRule() {
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
        return checkMandatoryPattern(getTableName(change), change);
    }

    @Override
    public String getMessage(AbstractChange change) {
        return formatMessage(getTableName(change), getConfig().getPatternString());
    }

    private String getTableName(AbstractChange change) {
        if (change instanceof CreateTableChange) {
            return ((CreateTableChange) change).getTableName();
        } else {
            return ((RenameTableChange) change).getNewTableName();
        }
    }
}
