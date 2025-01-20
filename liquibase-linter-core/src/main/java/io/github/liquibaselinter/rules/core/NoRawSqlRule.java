package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.LintRuleViolationGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import liquibase.change.Change;
import liquibase.change.core.RawSQLChange;
import liquibase.change.core.SQLFileChange;

@AutoService(ChangeRule.class)
public class NoRawSqlRule implements ChangeRule {

    private static final String NAME = "no-raw-sql";
    private static final String DEFAULT_MESSAGE =
        "Raw sql change types are not allowed, use appropriate Liquibase change types";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        if (isInvalid(change)) {
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
            return Collections.singleton(violations.withFormattedMessage());
        }

        return Collections.emptyList();
    }

    private boolean isInvalid(Change change) {
        return change instanceof RawSQLChange || change instanceof SQLFileChange;
    }
}
