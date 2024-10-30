package io.github.liquibaselinter.config.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.rules.AbstractLintRule;
import io.github.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.core.AbstractModifyDataChange;

import java.util.Locale;

@SuppressWarnings("rawtypes")
@AutoService({ChangeRule.class})
public class ModifyDataStartsWithWhere extends AbstractLintRule implements ChangeRule<AbstractModifyDataChange> {
    private static final String NAME = "modify-data-starts-with-where";
    private static final String MESSAGE = "Modify data where starts with where clause, that's probably a mistake";

    public ModifyDataStartsWithWhere() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<AbstractModifyDataChange> getChangeType() {
        return AbstractModifyDataChange.class;
    }

    @Override
    public boolean invalid(AbstractModifyDataChange modifyDataChange) {
        return modifyDataChange.getWhere() != null && modifyDataChange.getWhere().toLowerCase(Locale.ENGLISH).startsWith("where");
    }

}
