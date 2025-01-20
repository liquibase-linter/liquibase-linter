package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleViolationGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;
import liquibase.change.Change;
import liquibase.change.core.AbstractModifyDataChange;

@AutoService(ChangeRule.class)
public class ModifyDataEnforceWhere implements ChangeRule {

    private static final String NAME = "modify-data-enforce-where";
    private static final String DEFAULT_MESSAGE = "Modify data on table '%s' must have a where condition";

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
        if (isInvalid(ruleConfig, modifyDataChange)) {
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
            return Collections.singleton(violations.withFormattedMessage(modifyDataChange.getTableName()));
        }

        return Collections.emptyList();
    }

    private boolean isInvalid(RuleConfig ruleConfig, AbstractModifyDataChange modifyDataChange) {
        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        return (
            matchesTableName(ruleConfig, modifyDataChange.getTableName()) &&
            (ruleChecker.checkNotBlank(modifyDataChange.getWhere()) ||
                ruleChecker.checkPattern(modifyDataChange.getWhere(), modifyDataChange))
        );
    }

    private boolean matchesTableName(RuleConfig ruleConfig, String tableName) {
        if (ruleConfig.getValues() == null) {
            return true;
        }
        return ruleConfig.getValues().stream().anyMatch(value -> Pattern.compile(value).matcher(tableName).matches());
    }
}
