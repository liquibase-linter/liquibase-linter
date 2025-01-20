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
public class PrimaryKeyTablespaceRule implements ChangeRule {

    private static final String NAME = "primary-key-tablespace";
    private static final String DEFAULT_MESSAGE = "Tablespace '%s' is empty or does not follow pattern '%s'";

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
        LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
        LintRuleMessageGenerator messageGenerator = new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig);
        return extractTablespacesFrom(change)
            .stream()
            .filter(tablespace -> ruleChecker.checkMandatoryPattern(tablespace, change))
            .map(tablespace ->
                new RuleViolation(messageGenerator.formattedMessage(tablespace, ruleConfig.effectivePatternFor(change)))
            )
            .collect(Collectors.toList());
    }

    private static List<String> primaryKeyTablespacesFromCreateTable(CreateTableChange change) {
        if (change.getColumns() == null) {
            return Collections.emptyList();
        }
        return change
            .getColumns()
            .stream()
            .map(ColumnConfig::getConstraints)
            .filter(Objects::nonNull)
            .filter(
                constraint ->
                    Boolean.TRUE.equals(constraint.isPrimaryKey()) || constraint.getPrimaryKeyTablespace() != null
            )
            .map(ConstraintsConfig::getPrimaryKeyTablespace)
            .distinct()
            .collect(Collectors.toList());
    }

    private Collection<String> extractTablespacesFrom(Change change) {
        if (change instanceof AddPrimaryKeyChange) {
            return Collections.singleton(((AddPrimaryKeyChange) change).getTablespace());
        }
        if (change instanceof CreateTableChange) {
            return primaryKeyTablespacesFromCreateTable((CreateTableChange) change);
        }
        return Collections.emptyList();
    }
}
