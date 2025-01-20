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
import liquibase.change.core.AddUniqueConstraintChange;

@AutoService(ChangeRule.class)
public class UniqueConstraintNameRule implements ChangeRule {

    private static final String NAME = "unique-constraint-name";
    private static final String DEFAULT_MESSAGE = "Unique constraint name does not follow pattern";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        if (!(change instanceof AddUniqueConstraintChange)) {
            return Collections.emptyList();
        }

        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        AddUniqueConstraintChange addUniqueConstraintChange = (AddUniqueConstraintChange) change;
        if (ruleChecker.checkMandatoryPattern(addUniqueConstraintChange.getConstraintName(), change)) {
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
            return Collections.singleton(
                violations.withFormattedMessage(
                    addUniqueConstraintChange.getConstraintName(),
                    ruleConfig.effectivePatternFor(change)
                )
            );
        }

        return Collections.emptyList();
    }
}
