package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeSetRule;
import io.github.liquibaselinter.rules.Changes;
import io.github.liquibaselinter.rules.LintRuleMessageGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import liquibase.changelog.ChangeSet;

import java.util.Collection;
import java.util.Collections;

@AutoService({ChangeSetRule.class})
public class IsolateDDLChangesRule implements ChangeSetRule {
    private static final String NAME = "isolate-ddl-changes";
    private static final String DEFAULT_MESSAGE = "Should only have a single ddl change per change set";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(ChangeSet changeSet, RuleConfig ruleConfig) {
        if (changeSet.getChanges().stream().filter(Changes::isDDL).count() > 1) {
            return Collections.singleton(new RuleViolation(getMessage(ruleConfig)));
        }
        return Collections.emptyList();
    }

    private String getMessage(RuleConfig ruleConfig) {
        return new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig).getMessage();
    }
}
