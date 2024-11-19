package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.core.AbstractModifyDataChange;

import java.util.regex.Pattern;

@SuppressWarnings("rawtypes")
@AutoService(ChangeRule.class)
public class ModifyDataEnforceWhere extends AbstractLintRule implements ChangeRule<AbstractModifyDataChange> {
    private static final String NAME = "modify-data-enforce-where";
    private static final String MESSAGE = "Modify data on table '%s' must have a where condition";

    public ModifyDataEnforceWhere() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean supports(Change change) {
        return change instanceof AbstractModifyDataChange;
    }

    @Override
    public boolean invalid(AbstractModifyDataChange modifyDataChange) {
        return matchesTableName(modifyDataChange.getTableName())
            && (checkNotBlank(modifyDataChange.getWhere()) || checkPattern(modifyDataChange.getWhere(), modifyDataChange));
    }

    @Override
    public String getMessage(AbstractModifyDataChange change) {
        return formatMessage(change.getTableName());
    }

    private boolean matchesTableName(String tableName) {
        return getConfig().getValues().stream().anyMatch(value -> Pattern.compile(value).matcher(tableName).matches());
    }

}
