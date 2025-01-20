package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeSetRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleViolationGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import liquibase.changelog.ChangeSet;

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
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
            return Collections.singleton(
                violations.withFormattedMessage(changeSet.getAuthor(), ruleConfig.getPatternString())
            );
        }
        return Collections.emptyList();
    }
}
