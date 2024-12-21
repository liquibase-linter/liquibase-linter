package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeSetRule;
import io.github.liquibaselinter.rules.Changes;
import io.github.liquibaselinter.rules.LintRuleMessageGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import liquibase.ContextExpression;
import liquibase.change.Change;
import liquibase.changelog.ChangeSet;

@AutoService({ ChangeSetRule.class })
public class SeparateDDLChangesRule implements ChangeSetRule {

    private static final String NAME = "separate-ddl-context";
    private static final String DEFAULT_MESSAGE = "Should have a ddl changes under ddl contexts";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(ChangeSet changeSet, RuleConfig ruleConfig) {
        if (isInvalid(changeSet, ruleConfig)) {
            return Collections.singleton(new RuleViolation(getMessage(ruleConfig)));
        }
        return Collections.emptyList();
    }

    public boolean isInvalid(ChangeSet changeSet, RuleConfig ruleConfig) {
        ContextExpression contextExpression = changeSet.getContextFilter();
        if (contextExpression == null || contextExpression.getContexts().isEmpty()) {
            return false;
        }
        Collection<String> contexts = contextExpression.getContexts();
        for (Change change : changeSet.getChanges()) {
            if (Changes.isDDL(change)) {
                for (String context : contexts) {
                    if (!ruleConfig.getPattern().map(pattern -> pattern.matcher(context).matches()).orElse(true)) {
                        return true;
                    }
                }
            } else if (Changes.isDML(change)) {
                for (String context : contexts) {
                    if (ruleConfig.getPattern().map(pattern -> pattern.matcher(context).matches()).orElse(false)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private String getMessage(RuleConfig ruleConfig) {
        return new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig).getMessage();
    }
}
