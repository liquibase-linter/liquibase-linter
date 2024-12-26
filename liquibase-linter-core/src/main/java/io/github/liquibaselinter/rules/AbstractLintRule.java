package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.checker.PatternChecker;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import liquibase.change.Change;

@Deprecated
public abstract class AbstractLintRule implements ChangeRule {

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

    @Deprecated
    private void configure(RuleConfig ruleConfig) {
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
        return Optional.ofNullable(patternChecker).map(checker -> checker.check(value, subject)).orElse(false);
    }

    protected boolean checkMandatoryPattern(String value, Object subject) {
        return checkNotBlank(value) || patternChecker.check(value, subject);
    }

    protected boolean checkMaxLength(String value) {
        return value != null && value.length() > getConfig().getMaxLength();
    }

    public String getMessage() {
        return getMessageTemplate();
    }

    private String getMessageTemplate() {
        return Optional.ofNullable(ruleConfig.getErrorMessage()).orElse(message);
    }

    protected String formatMessage(Object... stuff) {
        return String.format(
            getMessageTemplate(),
            Arrays.stream(stuff).map(thing -> Optional.ofNullable(thing).orElse("")).toArray()
        );
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        configure(ruleConfig); // TODO: for compatibility, remove this once all rules are updated
        if (!supports(change)) {
            return Collections.emptyList();
        }
        if (invalid(change)) {
            return Collections.singletonList(new RuleViolation(getMessage(change)));
        }
        return Collections.emptyList();
    }

    @Deprecated
    protected abstract boolean invalid(Change change);

    @Deprecated
    protected boolean supports(Change change) {
        return true;
    }

    @Deprecated
    protected String getMessage(Change change) {
        return getMessage();
    }
}
