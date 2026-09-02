package io.github.liquibaselinter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.liquibaselinter.config.ChangeSetIdentifier;
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

    @Nested
    class EnableAfter {

        @Test
        @SuppressWarnings("deprecation")
        void shouldResolveLegacyEnableAfterThroughGetEnableAfterChangelog() {
            RuleConfig ruleConfig = RuleConfig.builder().withEnableAfter("legacy.xml").build();

            assertThat(ruleConfig.getEnableAfter()).isEqualTo("legacy.xml");
            assertThat(ruleConfig.getEnableAfterChangelog()).isEqualTo("legacy.xml");
            assertThat(ruleConfig.isEnabledAfter()).isTrue();
        }

        @Test
        void shouldExposeEnableAfterChangelog() {
            RuleConfig ruleConfig = RuleConfig.builder().withEnableAfterChangelog("changelog.xml").build();

            assertThat(ruleConfig.getEnableAfterChangelog()).isEqualTo("changelog.xml");
        }

        @Test
        void shouldExposeEnableAfterChangeset() {
            ChangeSetIdentifier changeset = new ChangeSetIdentifier("changelog.xml", "create-table", "dba");
            RuleConfig ruleConfig = RuleConfig.builder().withEnableAfterChangeset(changeset).build();

            assertThat(ruleConfig.getEnableAfterChangeset()).isSameAs(changeset);
            assertThat(ruleConfig.isEnabledAfter()).isTrue();
        }

        @Test
        void shouldReportNotEnabledAfterWhenNoOptionSet() {
            assertThat(RuleConfig.builder().build().isEnabledAfter()).isFalse();
        }

        @Test
        @SuppressWarnings("deprecation")
        void shouldRejectMoreThanOneEnableAfterOption() {
            assertThatThrownBy(() ->
                RuleConfig.builder().withEnableAfter("legacy.xml").withEnableAfterChangelog("changelog.xml").build()
            )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("rule configuration");
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
