package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.RuleViolation;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HasContextRuleTest {

    private final HasContextRule rule = new HasContextRule();

    @DisplayName("Should pass when a context has been provided on the changeSet")
    @Test
    void shouldPassWithContexts() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getContextFilter().isEmpty()).thenReturn(false);

        assertThat(rule.check(changeSet, RuleConfig.EMPTY)).isEmpty();
    }

    @DisplayName("Should fail when a context has not been provided on the changeSet")
    @Test
    void shouldFailWithNoContexts() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getContextFilter().isEmpty()).thenReturn(true);

        assertThat(rule.check(changeSet, RuleConfig.EMPTY))
            .extracting(RuleViolation::message)
            .containsExactly("Should have at least one context on the change set");
    }
}
