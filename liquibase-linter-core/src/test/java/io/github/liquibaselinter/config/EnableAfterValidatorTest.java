package io.github.liquibaselinter.config;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class EnableAfterValidatorTest {

    private static final ChangeSetIdentifier A_CHANGESET = new ChangeSetIdentifier("changelog.xml", "an-id", "dba");

    @Test
    void shouldAllowNoneSet() {
        assertThatCode(() ->
            EnableAfterValidator.requireAtMostOne("configuration", null, null, null)
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldAllowExactlyOneSet() {
        assertThatCode(() ->
            EnableAfterValidator.requireAtMostOne("configuration", "legacy.xml", null, null)
        ).doesNotThrowAnyException();
        assertThatCode(() ->
            EnableAfterValidator.requireAtMostOne("configuration", null, "changelog.xml", null)
        ).doesNotThrowAnyException();
        assertThatCode(() ->
            EnableAfterValidator.requireAtMostOne("configuration", null, null, A_CHANGESET)
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldTreatEmptyStringsAsUnset() {
        assertThatCode(() ->
            EnableAfterValidator.requireAtMostOne("configuration", "", "changelog.xml", null)
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldRejectMoreThanOneSet() {
        assertThatThrownBy(() ->
            EnableAfterValidator.requireAtMostOne("rule configuration", "legacy.xml", "changelog.xml", null)
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Only one of")
            .hasMessageContaining("rule configuration");

        assertThatThrownBy(() ->
            EnableAfterValidator.requireAtMostOne("configuration", null, "changelog.xml", A_CHANGESET)
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
