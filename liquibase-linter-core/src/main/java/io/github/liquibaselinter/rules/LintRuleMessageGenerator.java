package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.Arrays;
import java.util.Optional;

public class LintRuleMessageGenerator {

    private final String defaultMessage;
    private final RuleConfig ruleConfig;

    public LintRuleMessageGenerator(String defaultMessage, RuleConfig ruleConfig) {
        this.defaultMessage = defaultMessage;
        this.ruleConfig = ruleConfig;
    }

    private String errorMessageTemplate() {
        return Optional.ofNullable(ruleConfig.getErrorMessage()).orElse(defaultMessage);
    }

    public String formattedMessage(Object... stuff) {
        return String.format(
            errorMessageTemplate(),
            Arrays.stream(stuff).map(thing -> Optional.ofNullable(thing).orElse("")).toArray()
        );
    }
}
