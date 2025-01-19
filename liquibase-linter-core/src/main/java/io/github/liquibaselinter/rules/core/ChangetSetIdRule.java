package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeSetRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleMessageGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import liquibase.changelog.ChangeSet;

@AutoService(ChangeSetRule.class)
public class ChangetSetIdRule implements ChangeSetRule {

    private static final String NAME = "changeset-id";
    private static final String DEFAULT_MESSAGE = "ChangeSet id '%s' does not follow pattern '%s'";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(ChangeSet changeSet, RuleConfig ruleConfig) {
        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        if (ruleChecker.checkMandatoryPattern(changeSet.getId(), changeSet)) {
            return Collections.singleton(new RuleViolation(getMessage(changeSet, ruleConfig)));
        }
        return Collections.emptyList();
    }

    private String getMessage(ChangeSet changeSet, RuleConfig ruleConfig) {
        return new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig).formattedMessage(
            changeSet.getId(),
            ruleConfig.getPatternString()
        );
    }
}
