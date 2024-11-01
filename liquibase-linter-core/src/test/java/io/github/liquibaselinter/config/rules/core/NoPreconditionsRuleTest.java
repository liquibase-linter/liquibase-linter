package io.github.liquibaselinter.config.rules.core;

import io.github.liquibaselinter.config.rules.RuleConfig;
import liquibase.change.core.InsertDataChange;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.precondition.Precondition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class NoPreconditionsRuleTest {
    @DisplayName("Should pass if no preconditions")
    @Test
    void shouldPassIfNoPreconditions() {
        NoPreconditionsRule rule = new NoPreconditionsRule();
        rule.configure(RuleConfig.builder().build());
        ChangeSet changeSet = new ChangeSet(mock(DatabaseChangeLog.class));
        changeSet.addChange(new InsertDataChange());
        assertFalse(rule.invalid(changeSet));
    }

    @DisplayName("Should fail on preconditions in changeSet")
    @Test
    void shouldFailWhenPreconditionsInChangeSet() {
        NoPreconditionsRule rule = new NoPreconditionsRule();
        rule.configure(RuleConfig.builder().build());
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getPreconditions().getNestedPreconditions()).thenReturn(Collections.singletonList(mock(Precondition.class)));
        assertTrue(rule.invalid(changeSet));
    }

    @DisplayName("Should fail on preconditions in changeLog")
    @Test
    void shouldFailWhenPreconditionsInChangeLog() {
        NoPreconditionsRule rule = new NoPreconditionsRule();
        rule.configure(RuleConfig.builder().build());
        DatabaseChangeLog changeLog = mock(DatabaseChangeLog.class, RETURNS_DEEP_STUBS);
        when(changeLog.getPreconditions().getNestedPreconditions()).thenReturn(Collections.singletonList(mock(Precondition.class)));
        assertTrue(rule.invalid(changeLog));
    }
}
