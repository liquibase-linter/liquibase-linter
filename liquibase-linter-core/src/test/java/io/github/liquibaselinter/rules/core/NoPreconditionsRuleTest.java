package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collections;
import liquibase.change.core.InsertDataChange;
import liquibase.changelog.ChangeSet;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.precondition.Precondition;
import liquibase.precondition.core.PreconditionContainer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NoPreconditionsRuleTest {

    private final NoPreconditionsRule rule = new NoPreconditionsRule();

    @DisplayName("Should pass if no preconditions")
    @Test
    void shouldPassIfNoPreconditions() {
        ChangeSet changeSet = new ChangeSet(mock(DatabaseChangeLog.class));
        changeSet.addChange(new InsertDataChange());

        assertThat(rule.check(changeSet, RuleConfig.EMPTY)).isEmpty();
    }

    @DisplayName("Should fail on preconditions in changeSet")
    @Test
    void shouldFailWhenPreconditionsInChangeSet() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getPreconditions().getNestedPreconditions()).thenReturn(
            Collections.singletonList(mock(Precondition.class))
        );

        assertThat(rule.check(changeSet, RuleConfig.EMPTY))
            .extracting(RuleViolation::message)
            .containsExactly("Preconditions are not allowed in this project");
    }

    @DisplayName("Should fail on preconditions in changeLog")
    @Test
    void shouldFailWhenPreconditionsInChangeLog() {
        DatabaseChangeLog changeLog = mock(DatabaseChangeLog.class, RETURNS_DEEP_STUBS);
        when(changeLog.getPreconditions().getNestedPreconditions()).thenReturn(
            Collections.singletonList(mock(Precondition.class))
        );

        assertThat(rule.check(changeLog, RuleConfig.EMPTY))
            .extracting(RuleViolation::message)
            .containsExactly("Preconditions are not allowed in this project");
    }

    @DisplayName("Should pass if no preconditions in changeLog")
    @Test
    void shouldPassIfNoPreconditionsInChangeLog() {
        assertThat(rule.check(mock(DatabaseChangeLog.class), RuleConfig.EMPTY)).isEmpty();
    }

    @DisplayName("Should pass on empty preconditions in changeLog")
    @Test
    void shouldPassWhenEmptyPreconditionsInChangeLog() {
        DatabaseChangeLog changeLog = new DatabaseChangeLog("changelog.xml");
        changeLog.setPreconditions(new PreconditionContainer());

        assertThat(rule.check(changeLog, RuleConfig.EMPTY)).isEmpty();
    }
}
