package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;
import liquibase.change.core.EmptyChange;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.FieldSource;

class UnsupportedChangeTest {

    private static final List<Arguments.ArgumentSet> changeRules = StreamSupport.stream(
        ServiceLoader.load(ChangeRule.class).spliterator(),
        false
    )
        .sorted(Comparator.comparing(ChangeRule::getName))
        .map(rule -> Arguments.argumentSet(rule.getName(), rule))
        .toList();

    @ParameterizedTest
    @FieldSource("changeRules")
    void shouldIgnoreUnsupportedChange(ChangeRule rule) {
        RuleConfig ruleConfig = RuleConfig.builder().withPattern("^NEVER_MATCHES$").withMaxLength(1).build();

        assertThat(rule).withConfig(ruleConfig).checkingChange(new EmptyChange()).hasNoViolations();
    }
}
