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
import liquibase.change.core.AddForeignKeyConstraintChange;

@AutoService(ChangeRule.class)
public class ForeignKeyNameRule implements ChangeRule {

    private static final String NAME = "foreign-key-name";
    private static final String DEFAULT_MESSAGE = "Foreign key name is missing or does not follow pattern";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        if (!(change instanceof AddForeignKeyConstraintChange)) {
            return Collections.emptyList();
        }
        AddForeignKeyConstraintChange addForeignKeyConstraintChange = (AddForeignKeyConstraintChange) change;
        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        final String constraintName = addForeignKeyConstraintChange.getConstraintName();
        if (ruleChecker.checkMandatoryPattern(constraintName, addForeignKeyConstraintChange)) {
            LintRuleMessageGenerator messageGenerator = new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig);
            return Collections.singleton(
                new RuleViolation(
                    messageGenerator.formattedMessage(
                        addForeignKeyConstraintChange.getConstraintName(),
                        ruleConfig.getPatternString()
                    )
                )
            );
        }

        return Collections.emptyList();
    }
}
