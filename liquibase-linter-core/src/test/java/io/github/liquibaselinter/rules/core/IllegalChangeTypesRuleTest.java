package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import java.util.Collections;
import liquibase.change.Change;
import liquibase.change.DatabaseChange;
import liquibase.change.core.LoadDataChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IllegalChangeTypesRuleTest {

    private final ChangeRule rule = new IllegalChangeTypesRule();

    @DisplayName("Null Illegal change type should be valid")
    @Test
    void nullIllegalChangeTypeShouldBeValid() {
        rule.configure(RuleConfig.EMPTY);

        assertThat(rule.invalid(new LoadDataChange())).isFalse();
    }

    @DisplayName("Empty Illegal change type should be valid")
    @Test
    void emptyIllegalChangeTypeShouldBeValid() {
        rule.configure(RuleConfig.builder().withValues(Collections.emptyList()).build());

        assertThat(rule.invalid(new LoadDataChange())).isFalse();
    }

    @DisplayName("Mismatch Illegal change type should be valid")
    @Test
    void mismatchIllegalChangeTypeShouldBeValid() {
        rule.configure(
            RuleConfig.builder().withValues(Collections.singletonList("liquibase.change.core.AddColumnChange")).build()
        );

        assertThat(rule.invalid(new LoadDataChange())).isFalse();
    }

    @DisplayName("Illegal change type should be invalid")
    @Test
    void illegalChangeTypeShouldBeInvalid() {
        rule.configure(
            RuleConfig.builder().withValues(Collections.singletonList("liquibase.change.core.LoadDataChange")).build()
        );

        assertThat(rule.invalid(new LoadDataChange())).isTrue();
    }

    @DisplayName("Illegal change type from database change annotation name")
    @Test
    void illegalChangeTypeFromDatabaseChangeAnnotationName() {
        rule.configure(
            RuleConfig.builder()
                .withValues(Collections.singletonList(LoadDataChange.class.getAnnotation(DatabaseChange.class).name()))
                .build()
        );

        assertThat(rule.invalid(new LoadDataChange())).isTrue();
    }
}
