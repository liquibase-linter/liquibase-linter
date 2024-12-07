package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeSetRule;
import io.github.liquibaselinter.rules.LintRuleMessageGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import liquibase.changelog.ChangeSet;

@AutoService({ ChangeSetRule.class })
public class HasContextRule implements ChangeSetRule {

    private static final String NAME = "has-context";
    private static final String DEFAULT_MESSAGE = "Should have at least one context on the change set";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(ChangeSet changeSet, RuleConfig ruleConfig) {
        if (changeSet.getContexts().isEmpty()) {
            return Collections.singleton(new RuleViolation(getMessage(ruleConfig)));
        }
        return Collections.emptyList();
    }

    private String getMessage(RuleConfig ruleConfig) {
        return new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig).getMessage();
    }
}
