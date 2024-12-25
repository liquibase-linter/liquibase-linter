package io.github.liquibaselinter.rules;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.Collection;
import liquibase.change.Change;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;

public class ChangeRuleAssert extends AbstractAssert<ChangeRuleAssert, ChangeRule> {

    private RuleConfig ruleConfig = RuleConfig.EMPTY;
    private Collection<RuleViolation> ruleViolations;

    private ChangeRuleAssert(ChangeRule actual) {
        super(actual, ChangeRuleAssert.class);
    }

    public static ChangeRuleAssert assertThat(ChangeRule actual) {
        return new ChangeRuleAssert(actual);
    }

    public ChangeRuleAssert hasName(String expectedRuleName) {
        Assertions.assertThat(actual.getName()).isEqualTo(expectedRuleName);
        return this;
    }

    public ChangeRuleAssert withConfig(RuleConfig config) {
        this.ruleConfig = config;

        return this;
    }

    public ChangeRuleAssert withConfig(RuleConfig.RuleConfigBuilder configBuilder) {
        this.ruleConfig = configBuilder.build();

        return this;
    }

    public ChangeRuleAssert checkingChange(Change change) {
        this.ruleViolations = actual.check(change, this.ruleConfig);

        return this;
    }

    public ChangeRuleAssert hasNoViolations() {
        Assertions.assertThat(this.ruleViolations).isEmpty();

        return this;
    }

    public ChangeRuleAssert hasViolations() {
        Assertions.assertThat(this.ruleViolations).isNotEmpty();

        return this;
    }

    public ChangeRuleAssert hasExactlyViolationsMessages(String... expectedViolationsMessages) {
        Assertions.assertThat(this.ruleViolations)
            .extracting(RuleViolation::message)
            .containsExactly(expectedViolationsMessages);

        return this;
    }
}
