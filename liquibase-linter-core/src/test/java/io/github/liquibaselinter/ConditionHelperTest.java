package io.github.liquibaselinter;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.ContextExpression;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ConditionHelperTest {

    @Nested
    class MatchesContext {

        @Test
        void shouldMatchSimpleContext() {
            ChangeSet changeSet = changeSetWithContext("foo");
            RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo')").build();

            assertThat(ConditionHelper.evaluateCondition(ruleConfig, changeSet)).isTrue();
        }

        @Test
        void shouldNotMatchNegativeSimpleContext() {
            ChangeSet changeSet = changeSetWithContext("!foo");
            RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo')").build();

            assertThat(ConditionHelper.evaluateCondition(ruleConfig, changeSet)).isFalse();
        }

        @Test
        void shouldNotNatchSimpleContextMismatch() {
            ChangeSet changeSet = changeSetWithContext("foo");
            RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('bar')").build();

            assertThat(ConditionHelper.evaluateCondition(ruleConfig, changeSet)).isFalse();
        }

        @Test
        void shouldNotMatchNoContext() {
            ChangeSet changeSet = changeSetWithNoContext();
            RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('bar')").build();

            assertThat(ConditionHelper.evaluateCondition(ruleConfig, changeSet)).isFalse();
        }

        @Test
        void shouldMatchMultipleAndContexts() {
            ChangeSet changeSet = changeSetWithContext("foo and bar");
            RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo', 'bar')").build();

            assertThat(ConditionHelper.evaluateCondition(ruleConfig, changeSet)).isTrue();
        }
    }

    private static ChangeSet changeSetWithContext(String contextExpression) {
        ChangeSet changeSet = changeSetWithNoContext();
        changeSet.setContextFilter(new ContextExpression(contextExpression));

        return changeSet;
    }

    private static ChangeSet changeSetWithNoContext() {
        return new ChangeSet(new DatabaseChangeLog());
    }
}
