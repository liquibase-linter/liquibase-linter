package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.function.Function;
import java.util.stream.Stream;
import liquibase.change.Change;
import liquibase.change.core.CreateSequenceChange;
import liquibase.change.core.RenameSequenceChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
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
        assertThat(rule).hasName("sequence-name");
    }

    @DisplayName("Sequence name must not be null")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromSequenceNameArgumentsProvider.class)
    void sequenceNameNameMustNotBeNull(Function<String, Change> changeFromSequenceName) {
        Change change = changeFromSequenceName.apply(null);

        assertThat(rule).checkingChange(change).hasViolations();
    }

    @DisplayName("Sequence name must follow pattern")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromSequenceNameArgumentsProvider.class)
    void sequenceNameNameMustFollowPattern(Function<String, Change> changeFromSequenceName) {
        RuleConfig ruleConfig = RuleConfig.builder().withPattern("^(?!SEQ)[A-Z_]+(?<!_)$").build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(changeFromSequenceName.apply("SEQ_INVALID"))
            .hasExactlyViolationsMessages(
                "Sequence name 'SEQ_INVALID' does not follow pattern '^(?!SEQ)[A-Z_]+(?<!_)$'"
            );

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange((changeFromSequenceName.apply("VALID")))
            .hasNoViolations();
    }

    @DisplayName("Sequence name rule should support formatted error message with pattern arg")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromSequenceNameArgumentsProvider.class)
    void sequenceNameNameRuleShouldReturnFormattedErrorMessage(Function<String, Change> changeFromSequenceName) {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withPattern("^(?!SEQ)[A-Z_]+(?<!_)$")
            .withErrorMessage("Sequence name '%s' must follow pattern '%s'")
            .build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(changeFromSequenceName.apply("SEQ_INVALID"))
            .hasExactlyViolationsMessages("Sequence name 'SEQ_INVALID' must follow pattern '^(?!SEQ)[A-Z_]+(?<!_)$'");
    }

    private static class ChangeFromSequenceNameArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of(Named.of(CreateSequenceChange.class.getSimpleName(), createSequenceChangeFactory())),
                Arguments.of(Named.of(RenameSequenceChange.class.getSimpleName(), renameSequenceChangeFactory()))
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
