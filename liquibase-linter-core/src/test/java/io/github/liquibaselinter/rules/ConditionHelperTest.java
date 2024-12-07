package io.github.liquibaselinter.rules;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.ContextExpression;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ConditionHelperTest {

    @Test
    void shouldMatchSimpleContext() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        when(changeSet.getContexts()).thenReturn(new ContextExpression("foo"));
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo')").build();
        assertThat(ConditionHelper.evaluateCondition(ruleConfig, changeSet)).isTrue();
    }

    @Test
    void shouldNotMatchNegativeSimpleContext() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        when(changeSet.getContexts()).thenReturn(new ContextExpression("!foo"));
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo')").build();
        assertThat(ConditionHelper.evaluateCondition(ruleConfig, changeSet)).isFalse();
    }

    @Test
    void shouldNotNatchSimpleContextMismatch() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        when(changeSet.getContexts()).thenReturn(new ContextExpression("foo"));
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('bar')").build();
        assertThat(ConditionHelper.evaluateCondition(ruleConfig, changeSet)).isFalse();
    }

    @Test
    void shouldNotMatchNoContext() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('bar')").build();
        assertThat(ConditionHelper.evaluateCondition(ruleConfig, changeSet)).isFalse();
    }

    @Test
    void shouldMatchMultipleAndContexts() {
        ChangeSet changeSet = Mockito.mock(ChangeSet.class);
        when(changeSet.getContexts()).thenReturn(new ContextExpression("foo and bar"));
        RuleConfig ruleConfig = RuleConfig.builder().withCondition("matchesContext('foo', 'bar')").build();
        assertThat(ConditionHelper.evaluateCondition(ruleConfig, changeSet)).isTrue();
    }
}
