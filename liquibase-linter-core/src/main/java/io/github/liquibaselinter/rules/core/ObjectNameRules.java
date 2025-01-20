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
import liquibase.change.ColumnConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.AddForeignKeyConstraintChange;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.change.core.AddUniqueConstraintChange;
import liquibase.change.core.CreateIndexChange;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.CreateViewChange;
import liquibase.change.core.MergeColumnChange;
import liquibase.change.core.RenameColumnChange;
import liquibase.change.core.RenameViewChange;

public class ObjectNameRules {

    private static Collection<String> getObjectNames(Change change) {
        if (change instanceof AddColumnChange) {
            return ((AddColumnChange) change).getColumns()
                .stream()
                .map(ColumnConfig::getName)
                .collect(Collectors.toList());
        }
        if (change instanceof AddForeignKeyConstraintChange) {
            return Collections.singletonList(((AddForeignKeyConstraintChange) change).getConstraintName());
        }
        if (change instanceof AddPrimaryKeyChange) {
            return Collections.singletonList(((AddPrimaryKeyChange) change).getConstraintName());
        }
        if (change instanceof AddUniqueConstraintChange) {
            return Collections.singletonList(((AddUniqueConstraintChange) change).getConstraintName());
        }
        if (change instanceof CreateTableChange) {
            return ((CreateTableChange) change).getColumns()
                .stream()
                .map(ColumnConfig::getName)
                .collect(Collectors.toList());
        }
        if (change instanceof MergeColumnChange) {
            return Collections.singletonList(((MergeColumnChange) change).getFinalColumnName());
        }
        if (change instanceof RenameColumnChange) {
            return Collections.singletonList(((RenameColumnChange) change).getNewColumnName());
        }
        if (change instanceof RenameViewChange) {
            return Collections.singletonList(((RenameViewChange) change).getNewViewName());
        }
        if (change instanceof CreateViewChange) {
            return Collections.singletonList(((CreateViewChange) change).getViewName());
        }
        if (change instanceof CreateIndexChange) {
            return Collections.singletonList(((CreateIndexChange) change).getIndexName());
        }
        return Collections.emptyList();
    }

    @AutoService(ChangeRule.class)
    public static class ObjectNameRule implements ChangeRule {

        private static final String NAME = "object-name";
        private static final String DEFAULT_MESSAGE = "Object name does not follow pattern";

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
            LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
            return getObjectNames(change)
                .stream()
                .filter(objectName -> ruleChecker.checkMandatoryPattern(objectName, change))
                .map(objectName -> violations.withFormattedMessage(objectName, ruleConfig.getPatternString()))
                .collect(Collectors.toList());
        }
    }

    @AutoService(ChangeRule.class)
    public static class ObjectNameLengthRule implements ChangeRule {

        private static final String NAME = "object-name-length";
        private static final String DEFAULT_MESSAGE = "Object name '%s' must be less than %d characters";

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
            LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
            return getObjectNames(change)
                .stream()
                .filter(ruleChecker::checkMaxLength)
                .map(objectName -> violations.withFormattedMessage(objectName, ruleConfig.getMaxLength()))
                .collect(Collectors.toList());
        }
    }
}
