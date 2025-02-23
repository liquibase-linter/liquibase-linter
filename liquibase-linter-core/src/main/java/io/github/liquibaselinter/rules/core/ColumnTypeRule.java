package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleViolationGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import liquibase.change.Change;
import liquibase.change.ChangeWithColumns;
import liquibase.change.ColumnConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;

@AutoService(ChangeRule.class)
public class ColumnTypeRule implements ChangeRule {

    private static final String NAME = "column-type";
    private static final String DEFAULT_MESSAGE = "Type '%s' of column '%s' does not follow pattern '%s'";

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

        return getColumns(change)
            .stream()
            .filter(column -> ruleChecker.checkMandatoryPattern(column.getType(), change))
            .map(column ->
                violations.withFormattedMessage(column.getType(), column.getName(), ruleConfig.getPatternString())
            )
            .collect(Collectors.toList());
    }

    private boolean supports(Change change) {
        return (change instanceof AddColumnChange || change instanceof CreateTableChange);
    }

    private Set<ColumnConfig> getColumns(Change change) {
        if (change instanceof ChangeWithColumns<?>) {
            return new HashSet<>(((ChangeWithColumns<? extends ColumnConfig>) change).getColumns());
        }
        return new HashSet<>();
    }
}
