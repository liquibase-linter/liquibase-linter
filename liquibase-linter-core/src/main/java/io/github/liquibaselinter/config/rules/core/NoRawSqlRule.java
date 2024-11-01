package io.github.liquibaselinter.config.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.rules.AbstractLintRule;
import io.github.liquibaselinter.config.rules.ChangeRule;
import liquibase.change.Change;
import liquibase.change.core.RawSQLChange;
import liquibase.change.core.SQLFileChange;

@SuppressWarnings("rawtypes")
@AutoService({ChangeRule.class})
public class NoRawSqlRule extends AbstractLintRule implements ChangeRule<Change> {
    private static final String NAME = "no-raw-sql";
    private static final String MESSAGE = "Raw sql change types are not allowed, use appropriate Liquibase change types";

    public NoRawSqlRule() {
        super(NAME, MESSAGE);
    }

    @Override
    public Class<Change> getChangeType() {
        return Change.class;
    }

    @Override
    public boolean invalid(Change change) {
        return change instanceof RawSQLChange || change instanceof SQLFileChange;
    }

}
