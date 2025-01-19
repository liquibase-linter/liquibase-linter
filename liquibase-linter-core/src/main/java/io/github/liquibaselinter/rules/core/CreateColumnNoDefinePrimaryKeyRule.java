package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.LintRuleMessageGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;
import liquibase.change.Change;
import liquibase.change.ChangeWithColumns;
import liquibase.change.ColumnConfig;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;

@AutoService(ChangeRule.class)
public class CreateColumnNoDefinePrimaryKeyRule implements ChangeRule {

    private static final String NAME = "create-column-no-define-primary-key";
    private static final String DEFAULT_MESSAGE =
        "Add column must not use primary key attribute. Instead use AddPrimaryKey change type";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        if (!supports(change)) {
            return Collections.emptyList();
        }

        LintRuleMessageGenerator messageGenerator = new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig);
        ChangeWithColumns<?> changeWithColumns = (ChangeWithColumns<?>) change;
        return changeWithColumns
            .getColumns()
            .stream()
            .map(ColumnConfig::getConstraints)
            .filter(Objects::nonNull)
            .filter(constraints -> Boolean.TRUE.equals(constraints.isPrimaryKey()))
            .map(constraints -> new RuleViolation(messageGenerator.formattedMessage()))
            .collect(Collectors.toList());
    }

    private boolean supports(Change change) {
        return change instanceof CreateTableChange || change instanceof AddColumnChange;
    }
}
