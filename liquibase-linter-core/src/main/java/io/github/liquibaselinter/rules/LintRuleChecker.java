package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.checker.PatternChecker;
import java.util.Optional;

public class LintRuleChecker {

    private final RuleConfig ruleConfig;
    private final PatternChecker patternChecker;

    public LintRuleChecker(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
        if (ruleConfig.hasPattern()) {
            this.patternChecker = new PatternChecker(ruleConfig);
        } else {
            this.patternChecker = null;
        }
    }

    public boolean checkBlank(String value) {
        return value != null && !value.isEmpty();
    }

    public boolean checkNotBlank(String value) {
        return value == null || value.isEmpty();
    }

    public boolean checkPattern(String value, Object subject) {
        return Optional.ofNullable(patternChecker).map(checker -> checker.check(value, subject)).orElse(false);
    }

    public boolean checkMandatoryPattern(String value, Object subject) {
        return checkNotBlank(value) || patternChecker.check(value, subject);
    }

    public boolean checkMaxLength(String value) {
        return value != null && value.length() > ruleConfig.getMaxLength();
    }
}
