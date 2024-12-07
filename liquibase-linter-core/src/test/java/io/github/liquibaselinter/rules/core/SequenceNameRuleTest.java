package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.function.Function;
import java.util.stream.Stream;
import liquibase.change.Change;
import liquibase.change.core.CreateSequenceChange;
import liquibase.change.core.RenameSequenceChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class SequenceNameRuleTest {

    private final SequenceNameRule rule = new SequenceNameRule();

    @Test
    void shouldHaveName() {
        assertThat(rule.getName()).isEqualTo("sequence-name");
    }

    @DisplayName("Sequence name must not be null")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromSequenceNameArgumentsProvider.class)
    void sequenceNameNameMustNotBeNull(String changeName, Function<String, Change> changeFromSequenceName) {
        Change change = changeFromSequenceName.apply(null);

        assertThat(rule.invalid(change)).isTrue();
    }

    @DisplayName("Sequence name must follow pattern")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromSequenceNameArgumentsProvider.class)
    void sequenceNameNameMustFollowPattern(String changeName, Function<String, Change> changeFromSequenceName) {
        rule.configure(RuleConfig.builder().withPattern("^(?!SEQ)[A-Z_]+(?<!_)$").build());

        assertThat(rule.invalid(changeFromSequenceName.apply("SEQ_INVALID"))).isTrue();
        assertThat(rule.getMessage(changeFromSequenceName.apply("SEQ_INVALID"))).isEqualTo(
            "Sequence name 'SEQ_INVALID' does not follow pattern '^(?!SEQ)[A-Z_]+(?<!_)$'"
        );

        assertThat(rule.invalid(changeFromSequenceName.apply("VALID"))).isFalse();
    }

    @DisplayName("Sequence name rule should support formatted error message with pattern arg")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromSequenceNameArgumentsProvider.class)
    void sequenceNameNameRuleShouldReturnFormattedErrorMessage(
        String changeName,
        Function<String, Change> changeFromSequenceName
    ) {
        Change change = changeFromSequenceName.apply("SEQ_INVALID");
        rule.configure(
            RuleConfig.builder()
                .withPattern("^(?!SEQ)[A-Z_]+(?<!_)$")
                .withErrorMessage("Sequence name '%s' must follow pattern '%s'")
                .build()
        );

        assertThat(rule.getMessage(change)).isEqualTo(
            "Sequence name 'SEQ_INVALID' must follow pattern '^(?!SEQ)[A-Z_]+(?<!_)$'"
        );
    }

    private static class ChangeFromSequenceNameArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of("CreateSequenceChange", createSequenceChangeFactory()),
                Arguments.of("RenameSequenceChange", renameSequenceChangeFactory())
            );
        }

        private Function<String, Change> createSequenceChangeFactory() {
            return sequenceName -> {
                CreateSequenceChange createSequenceChange = new CreateSequenceChange();
                createSequenceChange.setSequenceName(sequenceName);
                return createSequenceChange;
            };
        }

        private Function<String, Change> renameSequenceChangeFactory() {
            return sequenceName -> {
                RenameSequenceChange renameSequenceChange = new RenameSequenceChange();
                renameSequenceChange.setNewSequenceName(sequenceName);
                return renameSequenceChange;
            };
        }
    }
}
