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

    public String getPatternForMessage(Object subject) {
        return ruleConfig.hasDynamicPattern()
            ? ruleConfig.getDynamicPattern(ruleConfig.getDynamicValue(subject)).pattern()
            : ruleConfig.getPatternString();
    }

    public String getMessage() {
        return getMessageTemplate();
    }

    public String getMessageTemplate() {
        return Optional.ofNullable(ruleConfig.getErrorMessage()).orElse(defaultMessage);
    }

    public String formatMessage(Object... stuff) {
        return String.format(getMessageTemplate(), Arrays.stream(stuff)
            .map(thing -> Optional.ofNullable(thing).orElse(""))
            .toArray());
    }
}
