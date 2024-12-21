package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.Optional;
import liquibase.Contexts;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;

public final class ConditionHelper {

    private ConditionHelper() {}

    public static boolean evaluateCondition(RuleConfig ruleConfig, Change change) {
        return ruleConfig
            .getConditionalExpression()
            .map(expression -> expression.getValue(ConditionContext.from(change), boolean.class))
            .orElse(true);
    }

    public static boolean evaluateCondition(RuleConfig ruleConfig, ChangeSet changeSet) {
        return ruleConfig
            .getConditionalExpression()
            .map(expression -> expression.getValue(ConditionContext.from(changeSet), boolean.class))
            .orElse(true);
    }

    public static boolean evaluateCondition(RuleConfig ruleConfig, DatabaseChangeLog databaseChangeLog) {
        return ruleConfig
            .getConditionalExpression()
            .map(expression -> expression.getValue(ConditionContext.from(databaseChangeLog), boolean.class))
            .orElse(true);
    }

    private static final class ConditionContext {

        private final DatabaseChangeLog changeLog;
        private final ChangeSet changeSet;
        private final Change change;

        private ConditionContext(DatabaseChangeLog changeLog, ChangeSet changeSet, Change change) {
            this.changeLog = changeLog;
            this.changeSet = changeSet;
            this.change = change;
        }

        private static ConditionContext from(Change change) {
            return new ConditionContext(change.getChangeSet().getChangeLog(), change.getChangeSet(), change);
        }

        private static ConditionContext from(ChangeSet changeSet) {
            return new ConditionContext(changeSet.getChangeLog(), changeSet, null);
        }

        private static ConditionContext from(DatabaseChangeLog changeLog) {
            return new ConditionContext(changeLog, null, null);
        }

        public DatabaseChangeLog getChangeLog() {
            return changeLog;
        }

        public ChangeSet getChangeSet() {
            return changeSet;
        }

        public Change getChange() {
            return change;
        }

        public boolean matchesContext(String... toMatch) {
            return Optional.ofNullable(changeSet)
                .map(ChangeSet::getContextFilter)
                .map(contexts -> !contexts.isEmpty() && contexts.matches(new Contexts(toMatch)))
                .orElse(false);
        }
    }
}
