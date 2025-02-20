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
import liquibase.change.core.MergeColumnChange;
import liquibase.change.core.RenameColumnChange;

@AutoService(ChangeRule.class)
public class ColumnNameRule implements ChangeRule {

    private static final String NAME = "column-name";
    private static final String DEFAULT_MESSAGE = "Column name '%s' does not follow pattern '%s'";

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

        return getColumnNames(change)
            .stream()
            .filter(columnName -> ruleChecker.checkMandatoryPattern(columnName, change))
            .map(columnName -> violations.withFormattedMessage(columnName, ruleConfig.getPatternString()))
            .collect(Collectors.toList());
    }

    private boolean supports(Change change) {
        return (
            change instanceof AddColumnChange ||
            change instanceof RenameColumnChange ||
            change instanceof CreateTableChange ||
            change instanceof MergeColumnChange
        );
    }

    private Set<String> getColumnNames(Change change) {
        if (change instanceof ChangeWithColumns<?>) {
            return ((ChangeWithColumns<? extends ColumnConfig>) change).getColumns()
                .stream()
                .map(ColumnConfig::getName)
                .collect(Collectors.toSet());
        } else if (change instanceof RenameColumnChange) {
            return Collections.singleton(((RenameColumnChange) change).getNewColumnName());
        } else if (change instanceof MergeColumnChange) {
            return Collections.singleton(((MergeColumnChange) change).getFinalColumnName());
        }
        return new HashSet<>();
    }
}
