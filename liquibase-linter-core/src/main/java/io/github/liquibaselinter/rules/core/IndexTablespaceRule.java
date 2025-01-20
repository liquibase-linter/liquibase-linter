package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleViolationGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import liquibase.change.Change;
import liquibase.change.core.CreateIndexChange;

@AutoService(ChangeRule.class)
public class IndexTablespaceRule implements ChangeRule {

    private static final String NAME = "index-tablespace";
    private static final String DEFAULT_MESSAGE =
        "Tablespace '%s' of index '%s' is empty or does not follow pattern '%s'";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        if (!(change instanceof CreateIndexChange)) {
            return Collections.emptyList();
        }

        CreateIndexChange createIndexChange = (CreateIndexChange) change;
        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        if (ruleChecker.checkMandatoryPattern(createIndexChange.getTablespace(), change)) {
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
            return Collections.singleton(
                violations.withFormattedMessage(
                    createIndexChange.getTablespace(),
                    createIndexChange.getIndexName(),
                    ruleConfig.effectivePatternFor(change)
                )
            );
        }

        return Collections.emptyList();
    }
}
