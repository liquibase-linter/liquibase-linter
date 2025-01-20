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
import liquibase.change.ChangeWithColumns;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;

@AutoService(ChangeRule.class)
public class CreateColumnRemarksRule implements ChangeRule {

    private static final String NAME = "create-column-remarks";
    private static final String DEFAULT_MESSAGE = "Add column '%s' must contain remarks";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        if (!supports(change)) {
            return Collections.emptyList();
        }

        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
        ChangeWithColumns<?> changeWithColumns = (ChangeWithColumns<?>) change;
        return changeWithColumns
            .getColumns()
            .stream()
            .filter(column -> ruleChecker.checkNotBlank(column.getRemarks()))
            .map(column -> violations.withFormattedMessage(column.getName()))
            .collect(Collectors.toList());
    }

    private boolean supports(Change change) {
        return change instanceof CreateTableChange || change instanceof AddColumnChange;
    }
}
