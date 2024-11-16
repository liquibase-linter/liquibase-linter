package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.rules.core.HasCommentRule;
import liquibase.change.core.TagDatabaseChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class HasCommentRuleTest {

    @DisplayName("Should pass when a comment has been provided on the changeSet")
    @Test
    void shouldPassWithPopulatedComment() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getComments()).thenReturn("Some comment");
        assertThat(new HasCommentRule().invalid(changeSet)).isFalse();
    }

    @DisplayName("Should pass when changeSet contains only a tagDatabase change")
    @Test
    void shouldPassWithTagDatabase() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getComments()).thenReturn(null);
        when(changeSet.getChanges()).thenReturn(Collections.singletonList(mock(TagDatabaseChange.class)));
        assertThat(new HasCommentRule().invalid(changeSet)).isFalse();
    }

    @DisplayName("Should fail when a comment has not been provided on the changeSet")
    @Test
    void shouldFailWithNoComment() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getComments()).thenReturn(null);
        assertThat(new HasCommentRule().invalid(changeSet)).isTrue();
    }

    @DisplayName("Should fail when a comment is blank on the changeSet")
    @Test
    void shouldFailWithBlankComment() {
        ChangeSet changeSet = mock(ChangeSet.class, RETURNS_DEEP_STUBS);
        when(changeSet.getComments()).thenReturn("");
        assertThat(new HasCommentRule().invalid(changeSet)).isTrue();
    }

}
