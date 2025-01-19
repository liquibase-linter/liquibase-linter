package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleMessageGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import liquibase.change.Change;
import liquibase.change.core.AddUniqueConstraintChange;

@AutoService(ChangeRule.class)
public class UniqueConstraintTablespaceRule implements ChangeRule {

    private static final String NAME = "unique-constraint-tablespace";
    private static final String DEFAULT_MESSAGE =
        "Tablespace '%s' of unique constraint '%s' is empty or does not follow pattern '%s'";

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
        if (ruleChecker.checkMandatoryPattern(addUniqueConstraintChange.getTablespace(), change)) {
            LintRuleMessageGenerator messageGenerator = new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig);
            return Collections.singleton(
                new RuleViolation(
                    messageGenerator.formattedMessage(
                        addUniqueConstraintChange.getTablespace(),
                        addUniqueConstraintChange.getConstraintName(),
                        messageGenerator.appliedPatternFor(change)
                    )
                )
            );
        }

        return Collections.emptyList();
    }
}
