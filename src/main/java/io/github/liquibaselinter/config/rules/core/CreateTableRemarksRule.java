package io.github.liquibaselinter.config.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.rules.AbstractLintRule;
import io.github.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.core.CreateTableChange;

@SuppressWarnings("rawtypes")
@AutoService({ChangeRule.class})
public class CreateTableRemarksRule extends AbstractLintRule implements ChangeRule<CreateTableChange> {
    private static final String NAME = "create-table-remarks";
    private static final String MESSAGE = "Create table must contain remark attribute";

    public CreateTableRemarksRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<CreateTableChange> getChangeType() {
        return CreateTableChange.class;
    }

    @Override
    public boolean invalid(CreateTableChange createTableChange) {
        return checkNotBlank(createTableChange.getRemarks());
    }

}
