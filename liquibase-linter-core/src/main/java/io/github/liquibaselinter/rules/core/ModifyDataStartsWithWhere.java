package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import java.util.Locale;
import liquibase.change.Change;
import liquibase.change.core.AbstractModifyDataChange;

@AutoService(ChangeRule.class)
public class ModifyDataStartsWithWhere extends AbstractLintRule implements ChangeRule {

    private static final String NAME = "modify-data-starts-with-where";
    private static final String MESSAGE = "Modify data where starts with where clause, that's probably a mistake";

    public ModifyDataStartsWithWhere() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean supports(Change change) {
        return change instanceof AbstractModifyDataChange;
    }

    @Override
    public boolean invalid(Change change) {
        AbstractModifyDataChange modifyDataChange = (AbstractModifyDataChange) change;
        return (
            modifyDataChange.getWhere() != null &&
            modifyDataChange.getWhere().toLowerCase(Locale.ENGLISH).startsWith("where")
        );
    }
}
