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
import liquibase.change.core.DropNotNullConstraintChange;

@AutoService(ChangeRule.class)
public class DropNotNullRequireColumnDataTypeRule implements ChangeRule {

    private static final String NAME = "drop-not-null-require-column-data-type";
    private static final String DEFAULT_MESSAGE =
        "Drop not null constraint column data type attribute must be populated";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        if (!(change instanceof DropNotNullConstraintChange)) {
            return Collections.emptyList();
        }
        DropNotNullConstraintChange dropNotNullConstraintChange = (DropNotNullConstraintChange) change;
        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        if (ruleChecker.checkNotBlank(dropNotNullConstraintChange.getColumnDataType())) {
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
            return Collections.singleton(violations.withFormattedMessage());
        }

        return Collections.emptyList();
    }
}
