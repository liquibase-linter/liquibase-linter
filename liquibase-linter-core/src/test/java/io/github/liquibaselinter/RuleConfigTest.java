package io.github.liquibaselinter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.liquibaselinter.config.ConditionContext;
import io.github.liquibaselinter.config.RuleConfig;
import liquibase.ContextExpression;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RuleConfigTest {

    @Nested
    class IsConditionSatisfiedWithContext {

        @Test
        void shouldMatchSimpleContext() {
            ChangeSet changeSet = changeSetWithLiquibaseContext("foo");
            RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo')").build();

            assertThat(ruleConfig.isConditionSatisfiedWithContext(ConditionContext.from(changeSet))).isTrue();
        }

        @Test
        void shouldNotMatchNegativeSimpleContext() {
            ChangeSet changeSet = changeSetWithLiquibaseContext("!foo");
            RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo')").build();

            assertThat(ruleConfig.isConditionSatisfiedWithContext(ConditionContext.from(changeSet))).isFalse();
        }

        @Test
        void shouldNotNatchSimpleContextMismatch() {
            ChangeSet changeSet = changeSetWithLiquibaseContext("foo");
            RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('bar')").build();

            assertThat(ruleConfig.isConditionSatisfiedWithContext(ConditionContext.from(changeSet))).isFalse();
        }

        @Test
        void shouldNotMatchNoContext() {
            ChangeSet changeSet = changeSetWithNoContext();
            RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('bar')").build();

            assertThat(ruleConfig.isConditionSatisfiedWithContext(ConditionContext.from(changeSet))).isFalse();
        }

        @Test
        void shouldMatchMultipleAndContexts() {
            ChangeSet changeSet = changeSetWithLiquibaseContext("foo and bar");
            RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo', 'bar')").build();

            assertThat(ruleConfig.isConditionSatisfiedWithContext(ConditionContext.from(changeSet))).isTrue();
        }

        // Object.getClass() is outside the RESTRICTED+liquibase/io.github.liquibaselinter allowlist, so JEXL
        // denies it (returning null rather than throwing); the ensuing failure to unbox to boolean is what
        // ultimately stops the expression from ever resolving to a usable Class/Runtime reference.
        @Test
        void shouldDenyReflectionOutsideAllowedPackages() {
            ChangeSet changeSet = changeSetWithNoContext();
            RuleConfig ruleConfig = RuleConfig.builder()
                .withCondition("changeSet.getClass().forName('java.lang.Runtime')")
                .build();

            assertThatThrownBy(() ->
                ruleConfig.isConditionSatisfiedWithContext(ConditionContext.from(changeSet))
            ).isInstanceOf(RuntimeException.class);
        }
    }

    private static ChangeSet changeSetWithLiquibaseContext(String contextExpression) {
        ChangeSet changeSet = changeSetWithNoContext();
        changeSet.setContextFilter(new ContextExpression(contextExpression));

        return changeSet;
    }

    private static ChangeSet changeSetWithNoContext() {
        return new ChangeSet(new DatabaseChangeLog());
    }
}
