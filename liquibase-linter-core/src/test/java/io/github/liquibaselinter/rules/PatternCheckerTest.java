package io.github.liquibaselinter.rules;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class PatternCheckerTest {

    private final PatternChecker patternChecker = new PatternChecker(
        RuleConfig.builder().withPattern("^NEVER_MATCHES$").build()
    );

    @NullSource
    @ValueSource(strings = "")
    @ParameterizedTest
    void shouldNotReportMissingValue(String value) {
        assertThat(patternChecker.check(value, new Object())).isFalse();
    }

    @Test
    void shouldReportValueNotMatchingPattern() {
        assertThat(patternChecker.check("FOO", new Object())).isTrue();
    }
}
