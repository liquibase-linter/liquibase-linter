package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.LintRuleMessageGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import liquibase.change.Change;
import liquibase.change.DatabaseChange;

@AutoService(ChangeRule.class)
public class IllegalChangeTypesRule implements ChangeRule {

    private static final String NAME = "illegal-change-types";
    private static final String DEFAULT_MESSAGE = "Change type '%s' is not allowed in this project";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        if (ruleConfig.getValues() == null) {
            return Collections.emptyList();
        }

        boolean changeIsNotAllowed = ruleConfig
            .getValues()
            .stream()
            .anyMatch(illegal -> getChangeName(change).equals(illegal) || getChangeClassName(change).equals(illegal));
        if (changeIsNotAllowed) {
            LintRuleMessageGenerator messageGenerator = new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig);
            return Collections.singleton(
                new RuleViolation(messageGenerator.formattedMessage(change.getClass().getCanonicalName()))
            );
        }

        return Collections.emptyList();
    }

    private static String getChangeClassName(Change change) {
        return change.getClass().getName();
    }

    private static String getChangeName(Change change) {
        return change.getClass().getAnnotation(DatabaseChange.class).name();
    }
}
