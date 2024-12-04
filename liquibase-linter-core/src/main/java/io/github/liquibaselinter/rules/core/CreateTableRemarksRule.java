package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.rules.AbstractLintRule;
import io.github.liquibaselinter.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.core.CreateTableChange;

@AutoService(ChangeRule.class)
public class CreateTableRemarksRule extends AbstractLintRule implements ChangeRule {
    private static final String NAME = "create-table-remarks";
    private static final String MESSAGE = "Create table must contain remark attribute";

    public CreateTableRemarksRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public boolean invalid(Change change) {
        CreateTableChange createTableChange = (CreateTableChange) change;
        return checkNotBlank(createTableChange.getRemarks());
    }

    @Override
    public boolean supports(Change change) {
        return change instanceof CreateTableChange;
    }
}
