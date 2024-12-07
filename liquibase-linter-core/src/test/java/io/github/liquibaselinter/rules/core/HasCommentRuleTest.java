package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collections;
import liquibase.change.core.TagDatabaseChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HasCommentRuleTest {

    private final HasCommentRule rule = new HasCommentRule();

    @DisplayName("Should pass when a comment has been provided on the changeSet")
    @Test
    void shouldPassWithPopulatedComment() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getComments()).thenReturn("Some comment");

        assertThat(rule.check(changeSet, RuleConfig.EMPTY)).isEmpty();
    }

    @DisplayName("Should pass when changeSet contains only a tagDatabase change")
    @Test
    void shouldPassWithTagDatabase() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getComments()).thenReturn(null);
        when(changeSet.getChanges()).thenReturn(Collections.singletonList(mock(TagDatabaseChange.class)));

        assertThat(rule.check(changeSet, RuleConfig.EMPTY)).isEmpty();
    }

    @DisplayName("Should fail when a comment has not been provided on the changeSet")
    @Test
    void shouldFailWithNoComment() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getComments()).thenReturn(null);

        assertThat(rule.check(changeSet, RuleConfig.EMPTY))
            .extracting(RuleViolation::message)
            .containsExactly("Change set must have a comment");
    }

    @DisplayName("Should fail when a comment is blank on the changeSet")
    @Test
    void shouldFailWithBlankComment() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getComments()).thenReturn("");

        assertThat(rule.check(changeSet, RuleConfig.EMPTY))
            .extracting(RuleViolation::message)
            .containsExactly("Change set must have a comment");
    }
}
