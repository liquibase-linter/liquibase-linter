package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.Arrays;
import java.util.Optional;

public class LintRuleViolationGenerator {

    private final String defaultMessage;
    private final RuleConfig ruleConfig;

    public LintRuleViolationGenerator(String defaultMessage, RuleConfig ruleConfig) {
        this.defaultMessage = defaultMessage;
        this.ruleConfig = ruleConfig;
    }

    private String errorMessageTemplate() {
        return Optional.ofNullable(ruleConfig.getErrorMessage()).orElse(defaultMessage);
    }

    /**
     * Generates a RuleViolation with the message configured in the rule config, or the default message if no
     * message is configured.
     * @param args Arguments referenced by the format specifiers in the message string.
     */
    public RuleViolation withFormattedMessage(Object... args) {
        return new RuleViolation(
            String.format(
                errorMessageTemplate(),
                Arrays.stream(args).map(arg -> Optional.ofNullable(arg).orElse("")).toArray()
            )
        );
    }
}
