package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleMessageGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import liquibase.change.Change;
import liquibase.change.ColumnConfig;
import liquibase.change.ConstraintsConfig;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.change.core.CreateTableChange;

@AutoService(ChangeRule.class)
public class PrimaryKeyNameRule implements ChangeRule {

    private static final String NAME = "primary-key-name";
    private static final String DEFAULT_MESSAGE = "Primary key name '%s' is missing or does not follow pattern '%s'";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        LintRuleMessageGenerator messageGenerator = new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig);
        return extractConstraintNamesFrom(change)
            .stream()
            .filter(constraintName -> ruleChecker.checkMandatoryPattern(constraintName, change))
            .map(constraintName ->
                new RuleViolation(
                    messageGenerator.formattedMessage(constraintName, ruleConfig.effectivePatternFor(change))
                )
            )
            .collect(Collectors.toList());
    }

    private Collection<String> extractConstraintNamesFrom(Change change) {
        if (change instanceof AddPrimaryKeyChange) {
            return Collections.singleton(((AddPrimaryKeyChange) change).getConstraintName());
        }
        if (change instanceof CreateTableChange) {
            return primaryKeyNamesFromCreateTable((CreateTableChange) change);
        }
        return Collections.emptyList();
    }

    private static List<String> primaryKeyNamesFromCreateTable(CreateTableChange change) {
        if (change.getColumns() == null) {
            return Collections.emptyList();
        }
        return change
            .getColumns()
            .stream()
            .map(ColumnConfig::getConstraints)
            .filter(Objects::nonNull)
            .filter(
                constraint -> Boolean.TRUE.equals(constraint.isPrimaryKey()) || constraint.getPrimaryKeyName() != null
            )
            .map(ConstraintsConfig::getPrimaryKeyName)
            .distinct()
            .collect(Collectors.toList());
    }
}
