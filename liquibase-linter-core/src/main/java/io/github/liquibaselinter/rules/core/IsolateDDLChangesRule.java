package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeSetRule;
import io.github.liquibaselinter.rules.Changes;
import io.github.liquibaselinter.rules.LintRuleViolationGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import liquibase.changelog.ChangeSet;

@AutoService(ChangeSetRule.class)
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
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
            return Collections.singleton(violations.withFormattedMessage());
        }
        return Collections.emptyList();
    }
}
