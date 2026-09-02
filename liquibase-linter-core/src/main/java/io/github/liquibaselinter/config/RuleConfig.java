package io.github.liquibaselinter.config;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

@JsonDeserialize(builder = RuleConfig.RuleConfigBuilder.class)
public final class RuleConfig {

    public static final RuleConfig EMPTY = builder().build();

    private static final String DYNAMIC_VALUE = "{{value}}";

    private final boolean enabled;
    private final String condition;
    private final String patternString;
    private final String columnCondition;
    private final String dynamicValue;
    private final List<String> values;
    private final Integer maxLength;
    private final String errorMessage;
    private final String enableAfter;
    private final String enableAfterChangelog;
    private final ChangeSetIdentifier enableAfterChangeset;
    private Pattern pattern;
    private Expression conditionExpression;
    private Expression columnConditionExpression;
    private Expression dynamicValueExpression;

    private RuleConfig(RuleConfigBuilder builder) {
        this.enabled = builder.enabled;
        this.errorMessage = builder.errorMessage;
        this.condition = builder.condition;
        this.columnCondition = builder.columnCondition;
        this.patternString = builder.pattern;
        this.dynamicValue = builder.dynamicValue;
        this.values = builder.values;
        this.maxLength = builder.maxLength;
        this.enableAfter = builder.enableAfter;
        this.enableAfterChangelog = builder.enableAfterChangelog;
        this.enableAfterChangeset = builder.enableAfterChangeset;
        EnableAfterValidator.requireAtMostOne(
            "rule configuration",
            builder.enableAfter,
            builder.enableAfterChangelog,
            builder.enableAfterChangeset
        );
    }

    public static RuleConfig enabled() {
        return builder().withEnabled(true).build();
    }

    public static RuleConfig disabled() {
        return builder().withEnabled(false).build();
    }

    public static RuleConfigBuilder builder() {
        return new RuleConfigBuilder();
    }

    public boolean hasDynamicPattern() {
        return patternString != null && patternString.contains(DYNAMIC_VALUE);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<String> getValues() {
        return values;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public String getPatternString() {
        return patternString;
    }

    public Optional<Expression> getConditionalColumnExpression() {
        if (columnCondition == null) {
            return Optional.empty();
        }
        if (columnConditionExpression == null) {
            columnConditionExpression = Expression.compile(columnCondition);
        }
        return Optional.of(columnConditionExpression);
    }

    private Optional<Expression> conditionalExpression() {
        if (condition == null) {
            return Optional.empty();
        }
        if (conditionExpression == null) {
            conditionExpression = Expression.compile(condition);
        }
        return Optional.of(conditionExpression);
    }

    public boolean isConditionSatisfiedWithContext(ConditionContext conditionContext) {
        return conditionalExpression()
            .map(expression -> expression.evaluateBoolean(conditionContext))
            .orElse(true);
    }

    private Optional<Expression> dynamicValueExpression() {
        if (dynamicValue == null) {
            return Optional.empty();
        }
        if (dynamicValueExpression == null) {
            dynamicValueExpression = Expression.compile(dynamicValue);
        }
        return Optional.of(dynamicValueExpression);
    }

    public Pattern getDynamicPattern(String value) {
        if (!hasDynamicPattern()) {
            throw new IllegalStateException("Pattern is not dynamic");
        }
        return Pattern.compile(getPatternString().replace(DYNAMIC_VALUE, value));
    }

    public String getDynamicValue(Object subject) {
        return dynamicValueExpression()
            .map(expression -> expression.evaluateString(subject))
            .orElse(null);
    }

    public Optional<Pattern> getPattern() {
        if (pattern == null && patternString != null && !hasDynamicPattern()) {
            pattern = Pattern.compile(patternString);
        }
        return Optional.ofNullable(pattern);
    }

    public boolean hasPattern() {
        return patternString != null && !patternString.isEmpty();
    }

    /**
     * @deprecated legacy option, use {@link #getEnableAfterChangelog()} instead. Removed in 1.0.
     */
    @Deprecated
    public String getEnableAfter() {
        return this.enableAfter;
    }

    /**
     * @return the changelog file after which this rule applies, resolved from whichever of
     * {@code enableAfterChangelog} or the legacy {@code enableAfter} is set, or {@code null} when the
     * rule is not gated on a changelog.
     */
    public String getEnableAfterChangelog() {
        return StringUtils.firstNonEmpty(enableAfterChangelog, enableAfter);
    }

    public ChangeSetIdentifier getEnableAfterChangeset() {
        return this.enableAfterChangeset;
    }

    public boolean isEnabledAfter() {
        return getEnableAfterChangelog() != null || enableAfterChangeset != null;
    }

    public String effectivePatternFor(Object subject) {
        if (!hasDynamicPattern()) {
            return getPatternString();
        }
        return getDynamicPattern(getDynamicValue(subject)).pattern();
    }

    public static class RuleConfigBuilder {

        private boolean enabled = true;
        private String errorMessage;
        private String condition;
        private String columnCondition;
        private String pattern;
        private String dynamicValue;
        private List<String> values;
        private Integer maxLength;
        private String enableAfter;
        private String enableAfterChangelog;
        private ChangeSetIdentifier enableAfterChangeset;

        @JsonProperty("enabled")
        public RuleConfigBuilder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        @JsonProperty("error-message")
        @JsonAlias("errorMessage")
        public RuleConfigBuilder withErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
            return this;
        }

        @JsonProperty("condition")
        public RuleConfigBuilder withCondition(String condition) {
            this.condition = condition;
            return this;
        }

        @JsonProperty("column-condition")
        @JsonAlias("columnCondition")
        public RuleConfigBuilder withColumnCondition(String columnCondition) {
            this.columnCondition = columnCondition;
            return this;
        }

        @JsonProperty("pattern")
        public RuleConfigBuilder withPattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        @JsonProperty("dynamic-value")
        @JsonAlias("dynamicValue")
        public RuleConfigBuilder withDynamicValue(String dynamicValue) {
            this.dynamicValue = dynamicValue;
            return this;
        }

        @JsonProperty("values")
        public RuleConfigBuilder withValues(List<String> values) {
            this.values = values;
            return this;
        }

        public RuleConfigBuilder withValues(String... values) {
            return withValues(Arrays.asList(values));
        }

        @JsonProperty("max-length")
        @JsonAlias("maxLength")
        public RuleConfigBuilder withMaxLength(Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        /**
         * @deprecated legacy option, use {@link #withEnableAfterChangelog(String)} instead. Removed in 1.0.
         */
        @Deprecated
        @JsonProperty("enable-after")
        @JsonAlias("enableAfter")
        public RuleConfigBuilder withEnableAfter(String enableAfter) {
            this.enableAfter = enableAfter;
            return this;
        }

        @JsonProperty("enable-after-changelog")
        @JsonAlias("enableAfterChangelog")
        public RuleConfigBuilder withEnableAfterChangelog(String enableAfterChangelog) {
            this.enableAfterChangelog = enableAfterChangelog;
            return this;
        }

        @JsonProperty("enable-after-changeset")
        @JsonAlias("enableAfterChangeset")
        public RuleConfigBuilder withEnableAfterChangeset(ChangeSetIdentifier enableAfterChangeset) {
            this.enableAfterChangeset = enableAfterChangeset;
            return this;
        }

        public RuleConfig build() {
            return new RuleConfig(this);
        }
    }
}
