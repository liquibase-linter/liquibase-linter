package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.checker.PatternChecker;

import java.util.Arrays;
import java.util.Optional;

public abstract class AbstractLintRule implements LintRule {
    private final String name;
    private final String message;
    protected RuleConfig ruleConfig;
    private PatternChecker patternChecker;

    protected AbstractLintRule(String name, String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void configure(RuleConfig ruleConfig) {
        this.ruleConfig = ruleConfig;
        if (ruleConfig.hasPattern()) {
            this.patternChecker = new PatternChecker(ruleConfig);
        } else {
            this.patternChecker = null;
        }
    }

    protected RuleConfig getConfig() {
        return ruleConfig;
    }

    protected String getPatternForMessage(Object subject) {
        return getConfig().hasDynamicPattern()
            ? getConfig().getDynamicPattern(getConfig().getDynamicValue(subject)).pattern()
            : getConfig().getPatternString();
    }

    protected boolean checkBlank(String value) {
        return value != null && !value.isEmpty();
    }

    protected boolean checkNotBlank(String value) {
        return value == null || value.isEmpty();
    }

    protected boolean checkPattern(String value, Object subject) {
        return Optional.ofNullable(patternChecker)
            .map(checker -> checker.check(value, subject))
            .orElse(false);
    }

    protected boolean checkMandatoryPattern(String value, Object subject) {
        return checkNotBlank(value) || patternChecker.check(value, subject);
    }

    protected boolean checkMaxLength(String value) {
        return value != null && value.length() > getConfig().getMaxLength();
    }

    @Override
    public String getMessage() {
        return getMessageTemplate();
    }

    private String getMessageTemplate() {
        return Optional.ofNullable(ruleConfig.getErrorMessage()).orElse(message);
    }

    protected String formatMessage(Object... stuff) {
        return String.format(getMessageTemplate(), Arrays.stream(stuff)
            .map(thing -> Optional.ofNullable(thing).orElse(""))
            .toArray());
    }
}
