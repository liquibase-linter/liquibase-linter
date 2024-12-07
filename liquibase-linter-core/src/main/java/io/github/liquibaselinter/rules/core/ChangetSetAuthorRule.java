package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeSetRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleMessageGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import liquibase.changelog.ChangeSet;

import java.util.Collection;
import java.util.Collections;

@AutoService(ChangeSetRule.class)
public class ChangetSetAuthorRule implements ChangeSetRule {
    private static final String NAME = "changeset-author";
    private static final String DEFAULT_MESSAGE = "ChangeSet author '%s' does not follow pattern '%s'";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(ChangeSet changeSet, RuleConfig ruleConfig) {
        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        if (ruleChecker.checkMandatoryPattern(changeSet.getAuthor(), changeSet)) {
            return Collections.singleton(new RuleViolation(getMessage(changeSet, ruleConfig)));
        }
        return Collections.emptyList();
    }

    private String getMessage(ChangeSet changeSet, RuleConfig ruleConfig) {
        LintRuleMessageGenerator messageGenerator = new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig);
        return messageGenerator.formatMessage(changeSet.getAuthor(), ruleConfig.getPatternString());
    }
}
