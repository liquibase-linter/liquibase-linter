package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import java.util.Collections;
import liquibase.change.core.LoadDataChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IllegalChangeTypesRuleTest {

    private final ChangeRule rule = new IllegalChangeTypesRule();

    @DisplayName("Null Illegal change type should be valid")
    @Test
    void nullIllegalChangeTypeShouldBeValid() {
        assertThat(rule).checkingChange(new LoadDataChange()).hasNoViolations();
    }

    @DisplayName("Empty Illegal change type should be valid")
    @Test
    void emptyIllegalChangeTypeShouldBeValid() {
        RuleConfig ruleConfig = RuleConfig.builder().withValues(Collections.emptyList()).build();

        assertThat(rule).withConfig(ruleConfig).checkingChange(new LoadDataChange()).hasNoViolations();
    }

    @DisplayName("Mismatch Illegal change type should be valid")
    @Test
    void mismatchIllegalChangeTypeShouldBeValid() {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withValues(Collections.singletonList("liquibase.change.core.AddColumnChange"))
            .build();

        assertThat(rule).withConfig(ruleConfig).checkingChange(new LoadDataChange()).hasNoViolations();
    }

    @DisplayName("Illegal change type should be invalid")
    @Test
    void illegalChangeTypeShouldBeInvalid() {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withValues(Collections.singletonList("liquibase.change.core.LoadDataChange"))
            .build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(new LoadDataChange())
            .hasExactlyViolationsMessages(
                "Change type 'liquibase.change.core.LoadDataChange' is not allowed in this project"
            );
    }

    @DisplayName("Illegal change type from database change annotation name")
    @Test
    void illegalChangeTypeFromDatabaseChangeAnnotationName() {
        RuleConfig ruleConfig = RuleConfig.builder().withValues(Collections.singletonList("loadData")).build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(new LoadDataChange())
            .hasExactlyViolationsMessages(
                "Change type 'liquibase.change.core.LoadDataChange' is not allowed in this project"
            );
    }
}
