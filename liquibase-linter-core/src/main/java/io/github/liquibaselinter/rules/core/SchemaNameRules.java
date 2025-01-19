package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleMessageGenerator;
import io.github.liquibaselinter.rules.RuleViolation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import liquibase.change.Change;
import liquibase.change.core.AbstractModifyDataChange;
import liquibase.change.core.AddAutoIncrementChange;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.AddDefaultValueChange;
import liquibase.change.core.AddForeignKeyConstraintChange;
import liquibase.change.core.AddLookupTableChange;
import liquibase.change.core.AddNotNullConstraintChange;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.change.core.AddUniqueConstraintChange;
import liquibase.change.core.AlterSequenceChange;
import liquibase.change.core.CreateIndexChange;
import liquibase.change.core.CreateProcedureChange;
import liquibase.change.core.CreateSequenceChange;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.CreateViewChange;
import liquibase.change.core.DropAllForeignKeyConstraintsChange;
import liquibase.change.core.DropColumnChange;
import liquibase.change.core.DropDefaultValueChange;
import liquibase.change.core.DropForeignKeyConstraintChange;
import liquibase.change.core.DropIndexChange;
import liquibase.change.core.DropNotNullConstraintChange;
import liquibase.change.core.DropPrimaryKeyChange;
import liquibase.change.core.DropProcedureChange;
import liquibase.change.core.DropSequenceChange;
import liquibase.change.core.DropTableChange;
import liquibase.change.core.DropUniqueConstraintChange;
import liquibase.change.core.DropViewChange;
import liquibase.change.core.InsertDataChange;
import liquibase.change.core.LoadDataChange;
import liquibase.change.core.MergeColumnChange;
import liquibase.change.core.ModifyDataTypeChange;
import liquibase.change.core.RenameColumnChange;
import liquibase.change.core.RenameSequenceChange;
import liquibase.change.core.RenameTableChange;
import liquibase.change.core.RenameViewChange;
import liquibase.change.core.SetColumnRemarksChange;
import liquibase.change.core.SetTableRemarksChange;

public class SchemaNameRules {

    private static Collection<String> getSchemaName(Change change) {
        if (change instanceof AbstractModifyDataChange) {
            return Collections.singletonList(((AbstractModifyDataChange) change).getSchemaName());
        }
        if (change instanceof AddAutoIncrementChange) {
            return Collections.singletonList(((AddAutoIncrementChange) change).getSchemaName());
        }
        if (change instanceof AddColumnChange) {
            return Collections.singletonList(((AddColumnChange) change).getSchemaName());
        }
        if (change instanceof AddDefaultValueChange) {
            return Collections.singletonList(((AddDefaultValueChange) change).getSchemaName());
        }
        if (change instanceof AddForeignKeyConstraintChange) {
            return Arrays.asList(
                ((AddForeignKeyConstraintChange) change).getBaseTableSchemaName(),
                ((AddForeignKeyConstraintChange) change).getReferencedTableSchemaName()
            );
        }
        if (change instanceof AddLookupTableChange) {
            return Arrays.asList(
                ((AddLookupTableChange) change).getExistingTableSchemaName(),
                ((AddLookupTableChange) change).getNewTableSchemaName()
            );
        }
        if (change instanceof AddNotNullConstraintChange) {
            return Collections.singletonList(((AddNotNullConstraintChange) change).getSchemaName());
        }
        if (change instanceof AddPrimaryKeyChange) {
            return Collections.singletonList(((AddPrimaryKeyChange) change).getSchemaName());
        }
        if (change instanceof AddUniqueConstraintChange) {
            return Collections.singletonList(((AddUniqueConstraintChange) change).getSchemaName());
        }
        if (change instanceof AlterSequenceChange) {
            return Collections.singletonList(((AlterSequenceChange) change).getSchemaName());
        }
        if (change instanceof CreateIndexChange) {
            return Collections.singletonList(((CreateIndexChange) change).getSchemaName());
        }
        if (change instanceof CreateProcedureChange) {
            return Collections.singletonList(((CreateProcedureChange) change).getSchemaName());
        }
        if (change instanceof CreateSequenceChange) {
            return Collections.singletonList(((CreateSequenceChange) change).getSchemaName());
        }
        if (change instanceof CreateTableChange) {
            return Collections.singletonList(((CreateTableChange) change).getSchemaName());
        }
        if (change instanceof CreateViewChange) {
            return Collections.singletonList(((CreateViewChange) change).getSchemaName());
        }
        if (change instanceof DropAllForeignKeyConstraintsChange) {
            return Collections.singletonList(((DropAllForeignKeyConstraintsChange) change).getBaseTableSchemaName());
        }
        if (change instanceof DropColumnChange) {
            return Collections.singletonList(((DropColumnChange) change).getSchemaName());
        }
        if (change instanceof DropDefaultValueChange) {
            return Collections.singletonList(((DropDefaultValueChange) change).getSchemaName());
        }
        if (change instanceof DropForeignKeyConstraintChange) {
            return Collections.singletonList(((DropForeignKeyConstraintChange) change).getBaseTableSchemaName());
        }
        if (change instanceof DropIndexChange) {
            return Collections.singletonList(((DropIndexChange) change).getSchemaName());
        }
        if (change instanceof DropNotNullConstraintChange) {
            return Collections.singletonList(((DropNotNullConstraintChange) change).getSchemaName());
        }
        if (change instanceof DropPrimaryKeyChange) {
            return Collections.singletonList(((DropPrimaryKeyChange) change).getSchemaName());
        }
        if (change instanceof DropProcedureChange) {
            return Collections.singletonList(((DropProcedureChange) change).getSchemaName());
        }
        if (change instanceof DropSequenceChange) {
            return Collections.singletonList(((DropSequenceChange) change).getSchemaName());
        }
        if (change instanceof DropTableChange) {
            return Collections.singletonList(((DropTableChange) change).getSchemaName());
        }
        if (change instanceof DropUniqueConstraintChange) {
            return Collections.singletonList(((DropUniqueConstraintChange) change).getSchemaName());
        }
        if (change instanceof DropViewChange) {
            return Collections.singletonList(((DropViewChange) change).getSchemaName());
        }
        if (change instanceof InsertDataChange) {
            return Collections.singletonList(((InsertDataChange) change).getSchemaName());
        }
        if (change instanceof LoadDataChange) {
            return Collections.singletonList(((LoadDataChange) change).getSchemaName());
        }
        if (change instanceof MergeColumnChange) {
            return Collections.singletonList(((MergeColumnChange) change).getSchemaName());
        }
        if (change instanceof ModifyDataTypeChange) {
            return Collections.singletonList(((ModifyDataTypeChange) change).getSchemaName());
        }
        if (change instanceof RenameColumnChange) {
            return Collections.singletonList(((RenameColumnChange) change).getSchemaName());
        }
        if (change instanceof RenameSequenceChange) {
            return Collections.singletonList(((RenameSequenceChange) change).getSchemaName());
        }
        if (change instanceof RenameTableChange) {
            return Collections.singletonList(((RenameTableChange) change).getSchemaName());
        }
        if (change instanceof RenameViewChange) {
            return Collections.singletonList(((RenameViewChange) change).getSchemaName());
        }
        if (change instanceof SetColumnRemarksChange) {
            return Collections.singletonList(((SetColumnRemarksChange) change).getSchemaName());
        }
        if (change instanceof SetTableRemarksChange) {
            return Collections.singletonList(((SetTableRemarksChange) change).getSchemaName());
        }
        return Collections.emptyList();
    }

    @AutoService(ChangeRule.class)
    public static class SchemaNameRule implements ChangeRule {

        private static final String NAME = "schema-name";
        private static final String DEFAULT_MESSAGE = "Schema name '%s' does not follow pattern '%s'";

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
            LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
            LintRuleMessageGenerator messageGenerator = new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig);
            return getSchemaName(change)
                .stream()
                .filter(schemaName -> ruleChecker.checkMandatoryPattern(schemaName, change))
                .distinct()
                .map(schemaName ->
                    new RuleViolation(messageGenerator.formatMessage(schemaName, ruleConfig.getPatternString()))
                )
                .collect(Collectors.toList());
        }
    }

    @AutoService(ChangeRule.class)
    public static class NoSchemaNameRule implements ChangeRule {

        private static final String NAME = "no-schema-name";
        private static final String DEFAULT_MESSAGE = "Schema names are not allowed in this project";

        @Override
        public String getName() {
            return NAME;
        }

        @Override
        public Collection<RuleViolation> check(Change change, RuleConfig ruleConfig) {
            LintRuleChecker ruleChecker = new LintRuleChecker(ruleConfig);
            LintRuleMessageGenerator messageGenerator = new LintRuleMessageGenerator(DEFAULT_MESSAGE, ruleConfig);
            return getSchemaName(change)
                .stream()
                .filter(ruleChecker::checkBlank)
                .distinct()
                .map(schemaName -> new RuleViolation(messageGenerator.getMessage()))
                .collect(Collectors.toList());
        }
    }
}
