package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleViolationGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import liquibase.change.Change;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.RenameTableChange;

@AutoService(ChangeRule.class)
public class TableNameRule implements ChangeRule {

    private static final String NAME = "table-name";
    private static final String DEFAULT_MESSAGE = "Table name does not follow pattern";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
        return getTablesName(change)
            .stream()
            .filter(tableName -> ruleChecker.checkMandatoryPattern(tableName, change))
            .map(tableName -> violations.withFormattedMessage(tableName, ruleConfig.getPatternString()))
            .collect(Collectors.toList());
    }

    private Collection<String> getTablesName(Change change) {
        if (change instanceof CreateTableChange) {
            return Collections.singleton(((CreateTableChange) change).getTableName());
        }
        if (change instanceof RenameTableChange) {
            return Collections.singleton(((RenameTableChange) change).getNewTableName());
        }
        return Collections.emptyList();
    }
}
