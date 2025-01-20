package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeSetRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleViolationGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import liquibase.ContextExpression;
import liquibase.changelog.ChangeSet;

@AutoService({ ChangeSetRule.class })
public class ValidContextRule implements ChangeSetRule {

    private static final String NAME = "valid-context";
    private static final String DEFAULT_MESSAGE = "Context does not follow pattern";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(ChangeSet changeSet, RuleConfig ruleConfig) {
        ContextExpression contextExpression = changeSet.getContextFilter();
        if (contextExpression != null) {
            LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
            return contextExpression
                .getContexts()
                .stream()
                .filter(context -> ruleChecker.checkPattern(context, changeSet))
                .map(context -> violations.withFormattedMessage())
                .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
