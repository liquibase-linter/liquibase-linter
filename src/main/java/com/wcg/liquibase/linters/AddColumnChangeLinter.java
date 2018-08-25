package com.wcg.liquibase.linters;

import com.wcg.liquibase.Linter;
import com.wcg.liquibase.config.rules.RuleRunner;
import liquibase.change.AddColumnConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.exception.ChangeLogParseException;

public class AddColumnChangeLinter implements Linter<AddColumnChange> {

    private final ColumnConfigLinter columnConfigLinter = new ColumnConfigLinter();
    private final ObjectNameLinter objectNameLinter = new ObjectNameLinter();

    @Override
    public void lint(AddColumnChange change, RuleRunner ruleRunner) throws ChangeLogParseException {
        for (AddColumnConfig addColumnConfig : change.getColumns()) {
            getObjectNameLinter().lintObjectName(addColumnConfig.getName(), change, ruleRunner);
        }
        getColumnConfigLinter().lintColumnConfig(change, ruleRunner);
    }

    ColumnConfigLinter getColumnConfigLinter() {
        return columnConfigLinter;
    }

    ObjectNameLinter getObjectNameLinter() {
        return objectNameLinter;
    }
}
