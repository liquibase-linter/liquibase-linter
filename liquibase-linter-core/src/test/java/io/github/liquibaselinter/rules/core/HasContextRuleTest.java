package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.rules.core.HasContextRule;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HasContextRuleTest {
    @DisplayName("Should pass when a context has been provided on the changeSet")
    @Test
    void shouldPassWithContexts() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getContexts().isEmpty()).thenReturn(false);
        assertThat(new HasContextRule().invalid(changeSet)).isFalse();
    }

    @DisplayName("Should fail when a context has not been provided on the changeSet")
    @Test
    void shouldFailWithNoContexts() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getContexts().isEmpty()).thenReturn(true);
        assertThat(new HasContextRule().invalid(changeSet)).isTrue();
    }
}
