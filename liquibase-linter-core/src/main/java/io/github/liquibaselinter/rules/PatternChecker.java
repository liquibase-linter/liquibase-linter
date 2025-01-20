package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.regex.Matcher;

class PatternChecker {

    private final RuleConfig ruleConfig;

    public PatternChecker(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
    }

    public boolean check(String value, Object subject) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        if (ruleConfig.hasDynamicPattern()) {
            String dynamicValue = ruleConfig.getDynamicValue(subject);
            return !ruleConfig.getDynamicPattern(dynamicValue).matcher(value).matches();
        }
        return !ruleConfig.getPattern().map(pattern -> pattern.matcher(value)).map(Matcher::matches).orElse(true);
    }
}
