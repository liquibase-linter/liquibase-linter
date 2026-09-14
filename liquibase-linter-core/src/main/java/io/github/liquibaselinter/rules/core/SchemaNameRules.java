package io.github.liquibaselinter.rules.core;

import com.google.auto.service.AutoService;
import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.ChangeRule;
import io.github.liquibaselinter.rules.LintRuleChecker;
import io.github.liquibaselinter.rules.LintRuleViolationGenerator;
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
        if (change instanceof AbstractModifyDataChange abstractModifyDataChange) {
            return Collections.singletonList(abstractModifyDataChange.getSchemaName());
        }
        if (change instanceof AddAutoIncrementChange addAutoIncrementChange) {
            return Collections.singletonList(addAutoIncrementChange.getSchemaName());
        }
        if (change instanceof AddColumnChange addColumnChange) {
            return Collections.singletonList(addColumnChange.getSchemaName());
        }
        if (change instanceof AddDefaultValueChange addDefaultValueChange) {
            return Collections.singletonList(addDefaultValueChange.getSchemaName());
        }
        if (change instanceof AddForeignKeyConstraintChange addForeignKeyConstraintChange) {
            return Arrays.asList(
                addForeignKeyConstraintChange.getBaseTableSchemaName(),
                addForeignKeyConstraintChange.getReferencedTableSchemaName()
            );
        }
        if (change instanceof AddLookupTableChange addLookupTableChange) {
            return Arrays.asList(
                addLookupTableChange.getExistingTableSchemaName(),
                addLookupTableChange.getNewTableSchemaName()
            );
        }
        if (change instanceof AddNotNullConstraintChange addNotNullConstraintChange) {
            return Collections.singletonList(addNotNullConstraintChange.getSchemaName());
        }
        if (change instanceof AddPrimaryKeyChange addPrimaryKeyChange) {
            return Collections.singletonList(addPrimaryKeyChange.getSchemaName());
        }
        if (change instanceof AddUniqueConstraintChange addUniqueConstraintChange) {
            return Collections.singletonList(addUniqueConstraintChange.getSchemaName());
        }
        if (change instanceof AlterSequenceChange alterSequenceChange) {
            return Collections.singletonList(alterSequenceChange.getSchemaName());
        }
        if (change instanceof CreateIndexChange createIndexChange) {
            return Collections.singletonList(createIndexChange.getSchemaName());
        }
        if (change instanceof CreateProcedureChange createProcedureChange) {
            return Collections.singletonList(createProcedureChange.getSchemaName());
        }
        if (change instanceof CreateSequenceChange createSequenceChange) {
            return Collections.singletonList(createSequenceChange.getSchemaName());
        }
        if (change instanceof CreateTableChange createTableChange) {
            return Collections.singletonList(createTableChange.getSchemaName());
        }
        if (change instanceof CreateViewChange createViewChange) {
            return Collections.singletonList(createViewChange.getSchemaName());
        }
        if (change instanceof DropAllForeignKeyConstraintsChange dropAllForeignKeyConstraintsChange) {
            return Collections.singletonList(dropAllForeignKeyConstraintsChange.getBaseTableSchemaName());
        }
        if (change instanceof DropColumnChange dropColumnChange) {
            return Collections.singletonList(dropColumnChange.getSchemaName());
        }
        if (change instanceof DropDefaultValueChange dropDefaultValueChange) {
            return Collections.singletonList(dropDefaultValueChange.getSchemaName());
        }
        if (change instanceof DropForeignKeyConstraintChange dropForeignKeyConstraintChange) {
            return Collections.singletonList(dropForeignKeyConstraintChange.getBaseTableSchemaName());
        }
        if (change instanceof DropIndexChange dropIndexChange) {
            return Collections.singletonList(dropIndexChange.getSchemaName());
        }
        if (change instanceof DropNotNullConstraintChange dropNotNullConstraintChange) {
            return Collections.singletonList(dropNotNullConstraintChange.getSchemaName());
        }
        if (change instanceof DropPrimaryKeyChange dropPrimaryKeyChange) {
            return Collections.singletonList(dropPrimaryKeyChange.getSchemaName());
        }
        if (change instanceof DropProcedureChange dropProcedureChange) {
            return Collections.singletonList(dropProcedureChange.getSchemaName());
        }
        if (change instanceof DropSequenceChange dropSequenceChange) {
            return Collections.singletonList(dropSequenceChange.getSchemaName());
        }
        if (change instanceof DropTableChange dropTableChange) {
            return Collections.singletonList(dropTableChange.getSchemaName());
        }
        if (change instanceof DropUniqueConstraintChange dropUniqueConstraintChange) {
            return Collections.singletonList(dropUniqueConstraintChange.getSchemaName());
        }
        if (change instanceof DropViewChange dropViewChange) {
            return Collections.singletonList(dropViewChange.getSchemaName());
        }
        if (change instanceof InsertDataChange insertDataChange) {
            return Collections.singletonList(insertDataChange.getSchemaName());
        }
        if (change instanceof LoadDataChange loadDataChange) {
            return Collections.singletonList(loadDataChange.getSchemaName());
        }
        if (change instanceof MergeColumnChange mergeColumnChange) {
            return Collections.singletonList(mergeColumnChange.getSchemaName());
        }
        if (change instanceof ModifyDataTypeChange modifyDataTypeChange) {
            return Collections.singletonList(modifyDataTypeChange.getSchemaName());
        }
        if (change instanceof RenameColumnChange renameColumnChange) {
            return Collections.singletonList(renameColumnChange.getSchemaName());
        }
        if (change instanceof RenameSequenceChange renameSequenceChange) {
            return Collections.singletonList(renameSequenceChange.getSchemaName());
        }
        if (change instanceof RenameTableChange renameTableChange) {
            return Collections.singletonList(renameTableChange.getSchemaName());
        }
        if (change instanceof RenameViewChange renameViewChange) {
            return Collections.singletonList(renameViewChange.getSchemaName());
        }
        if (change instanceof SetColumnRemarksChange setColumnRemarksChange) {
            return Collections.singletonList(setColumnRemarksChange.getSchemaName());
        }
        if (change instanceof SetTableRemarksChange setTableRemarksChange) {
            return Collections.singletonList(setTableRemarksChange.getSchemaName());
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
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
            return getSchemaName(change)
                .stream()
                .filter(schemaName -> ruleChecker.checkMandatoryPattern(schemaName, change))
                .distinct()
                .map(schemaName -> violations.withFormattedMessage(schemaName, ruleConfig.getPatternString()))
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
            LintRuleViolationGenerator violations = new LintRuleViolationGenerator(DEFAULT_MESSAGE, ruleConfig);
            return getSchemaName(change)
                .stream()
                .filter(ruleChecker::checkBlank)
                .distinct()
                .map(schemaName -> violations.withFormattedMessage())
                .collect(Collectors.toList());
        }
    }
}
