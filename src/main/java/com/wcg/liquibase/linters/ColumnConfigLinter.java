package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.RuleConfig;
import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.config.rules.RuleType;
import liquibase.change.Change;
import liquibase.change.ChangeWithColumns;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.exception.ChangeLogParseException;

import java.util.Map;

class ColumnConfigLinter {

    void lintColumnConfig(ChangeWithColumns<? extends ColumnConfig> change, Map<String, RuleConfig> ruleConfigs) throws ChangeLogParseException {
        for (ColumnConfig columnConfig : change.getColumns()) {
            final ConstraintsConfig constraints = columnConfig.getConstraints();
            RuleRunner.forChange(ruleConfigs, (Change) change)
                    .run(RuleType.CREATE_COLUMN_REMARKS, columnConfig.getRemarks())
                    .run(RuleType.CREATE_COLUMN_NULLABLE_CONSTRAINT, constraints)
                    .run(RuleType.CREATE_COLUMN_NO_DEFINE_PRIMARY_KEY, constraints);
        }
    }

}
