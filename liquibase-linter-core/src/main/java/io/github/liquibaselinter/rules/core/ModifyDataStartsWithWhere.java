package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.LintRuleViolationGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import liquibase.change.Change;
import liquibase.change.core.AbstractModifyDataChange;

@AutoService(ChangeRule.class)
public class ModifyDataStartsWithWhere implements ChangeRule {

    private static final String NAME = "modify-data-starts-with-where";
    private static final String DEFAULT_MESSAGE =
        "Modify data where starts with where clause, that's probably a mistake";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        if (!(change instanceof AbstractModifyDataChange)) {
            return Collections.emptyList();
        }

        AbstractModifyDataChange modifyDataChange = (AbstractModifyDataChange) change;
        if (isInvalid(modifyDataChange)) {
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
            return Collections.singleton(violations.withFormattedMessage(modifyDataChange.getTableName()));
        }

        return Collections.emptyList();
    }

    private static boolean isInvalid(Change change) {
        AbstractModifyDataChange modifyDataChange = (AbstractModifyDataChange) change;
        return (
            modifyDataChange.getWhere() != null &&
            modifyDataChange.getWhere().toLowerCase(Locale.ENGLISH).startsWith("where")
        );
    }
}
