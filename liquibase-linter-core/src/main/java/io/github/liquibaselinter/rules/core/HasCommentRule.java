package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeSetRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleMessageGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import liquibase.change.core.TagDatabaseChange;
import liquibase.changelog.ChangeSet;

@AutoService({ ChangeSetRule.class })
public class HasCommentRule implements ChangeSetRule {

    private static final String NAME = "has-comment";
    private static final String DEFAULT_MESSAGE = "Change set must have a comment";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(ChangeSet changeSet, RuleConfig ruleConfig) {
        if (changeSet.getChanges().stream().anyMatch(TagDatabaseChange.class::isInstance)) {
            /*
            https://github.com/whiteclarkegroup/liquibase-linter/issues/90
            tagDatabase changes cannot have any siblings in a changeSet - not even comments
            so we have to make an exception here
             */
            return Collections.emptyList();
        }

        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        if (ruleChecker.checkNotBlank(changeSet.getComments())) {
            return Collections.singleton(new RuleViolation(getMessage(ruleConfig)));
        }
        return Collections.emptyList();
    }

    private String getMessage(RuleConfig ruleConfig) {
        return new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig).formattedMessage();
    }
}
