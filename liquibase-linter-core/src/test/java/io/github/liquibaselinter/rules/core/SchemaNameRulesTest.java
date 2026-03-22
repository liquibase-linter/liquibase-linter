package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.core.SchemaNameRules.NoSchemaNameRule;
import io.github.liquibaselinter.rules.core.SchemaNameRules.SchemaNameRule;
import java.util.function.Function;
import java.util.stream.Stream;
import liquibase.change.Change;
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
import liquibase.change.core.DeleteDataChange;
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
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class SchemaNameRulesTest {

    @Nested
    class NoSchemaName {

        private final NoSchemaNameRule rule = new NoSchemaNameRule();

        @ParameterizedTest(name = "With {0}")
        @ArgumentsSource(ChangeWithSchemaNameArgumentsProvider.class)
        void definedSchemaNameShouldBeInvalid(Function<String, Change> changeWithSchemaNameFactory) {
            assertThat(rule)
                .checkingChange(changeWithSchemaNameFactory.apply("SCHEMA_NAME"))
                .hasExactlyViolationsMessages("Schema names are not allowed in this project");
        }

        @ParameterizedTest(name = "With {0}")
        @ArgumentsSource(ChangeWithSchemaNameArgumentsProvider.class)
        void nullSchemaNameShouldBeValid(Function<String, Change> changeWithSchemaNameFactory) {
            assertThat(rule).checkingChange(changeWithSchemaNameFactory.apply(null)).hasNoViolations();
        }

        @ParameterizedTest(name = "With {0}")
        @ArgumentsSource(ChangeWithSchemaNameArgumentsProvider.class)
        void emptySchemaNameShouldBeValid(Function<String, Change> changeWithSchemaNameFactory) {
            assertThat(rule).checkingChange(changeWithSchemaNameFactory.apply("")).hasNoViolations();
        }
    }

    @Nested
    class SchemaName {

        private final SchemaNameRule rule = new SchemaNameRule();
        private final RuleConfig ruleConfig = RuleConfig.builder().withPattern("^[A-Z_0-9]+$").build();

        @ParameterizedTest(name = "With {0}")
        @ArgumentsSource(ChangeWithSchemaNameArgumentsProvider.class)
        void definedSchemaNameShouldBeValid(Function<String, Change> changeWithSchemaNameFactory) {
            assertThat(rule)
                .withConfig(ruleConfig)
                .checkingChange(changeWithSchemaNameFactory.apply("SCHEMA_NAME"))
                .hasNoViolations();
        }

        @ParameterizedTest(name = "With {0}")
        @ArgumentsSource(ChangeWithSchemaNameArgumentsProvider.class)
        void nullSchemaNameShouldBeInvalid(Function<String, Change> changeWithSchemaNameFactory) {
            assertThat(rule)
                .withConfig(ruleConfig)
                .checkingChange(changeWithSchemaNameFactory.apply(null))
                .hasExactlyViolationsMessages("Schema name '' does not follow pattern '^[A-Z_0-9]+$'");
        }

        @ParameterizedTest(name = "With {0}")
        @ArgumentsSource(ChangeWithSchemaNameArgumentsProvider.class)
        void emptySchemaNameShouldBeInvalid(Function<String, Change> changeWithSchemaNameFactory) {
            assertThat(rule)
                .withConfig(ruleConfig)
                .checkingChange(changeWithSchemaNameFactory.apply(""))
                .hasExactlyViolationsMessages("Schema name '' does not follow pattern '^[A-Z_0-9]+$'");
        }
    }

    private static class ChangeWithSchemaNameArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of(Named.of(AddColumnChange.class.getSimpleName(), addColumnChangeFactory())),
                Arguments.of(Named.of(AddAutoIncrementChange.class.getSimpleName(), addAutoIncrementChangeFactory())),
                Arguments.of(Named.of(AddDefaultValueChange.class.getSimpleName(), addDefaultValueChangeFactory())),
                Arguments.of(
                    Named.of(
                        AddForeignKeyConstraintChange.class.getSimpleName(),
                        addForeignKeyConstraintChangeFactory()
                    )
                ),
                Arguments.of(Named.of(AddLookupTableChange.class.getSimpleName(), addLookupTableChangeFactory())),
                Arguments.of(
                    Named.of(AddNotNullConstraintChange.class.getSimpleName(), addNotNullConstraintChangeFactory())
                ),
                Arguments.of(Named.of(AddPrimaryKeyChange.class.getSimpleName(), addPrimaryKeyChangeFactory())),
                Arguments.of(
                    Named.of(AddUniqueConstraintChange.class.getSimpleName(), addUniqueConstraintChangeFactory())
                ),
                Arguments.of(Named.of(AlterSequenceChange.class.getSimpleName(), alterSequenceChangeFactory())),
                Arguments.of(Named.of(CreateTableChange.class.getSimpleName(), createTableChangeFactory())),
                Arguments.of(Named.of(MergeColumnChange.class.getSimpleName(), mergeColumnChangeFactory())),
                Arguments.of(Named.of(RenameColumnChange.class.getSimpleName(), renameColumnChangeFactory())),
                Arguments.of(Named.of(RenameViewChange.class.getSimpleName(), renameViewChangeFactory())),
                Arguments.of(Named.of(CreateViewChange.class.getSimpleName(), createViewChangeFactory())),
                Arguments.of(Named.of(CreateIndexChange.class.getSimpleName(), createIndexChangeFactory())),
                Arguments.of(Named.of(CreateProcedureChange.class.getSimpleName(), createProcedureChangeFactory())),
                Arguments.of(Named.of(CreateSequenceChange.class.getSimpleName(), createSequenceChangeFactory())),
                Arguments.of(Named.of(DeleteDataChange.class.getSimpleName(), deleteDataChangeFactory())),
                Arguments.of(
                    Named.of(
                        DropAllForeignKeyConstraintsChange.class.getSimpleName(),
                        dropAllForeignKeyConstraintsChangeFactory()
                    )
                ),
                Arguments.of(Named.of(DropColumnChange.class.getSimpleName(), dropColumnChangeFactory())),
                Arguments.of(Named.of(DropDefaultValueChange.class.getSimpleName(), dropDefaultValueChangeFactory())),
                Arguments.of(
                    Named.of(
                        DropForeignKeyConstraintChange.class.getSimpleName(),
                        dropForeignKeyConstraintChangeFactory()
                    )
                ),
                Arguments.of(Named.of(DropIndexChange.class.getSimpleName(), dropIndexChangeFactory())),
                Arguments.of(
                    Named.of(DropNotNullConstraintChange.class.getSimpleName(), dropNotNullConstraintChangeFactory())
                ),
                Arguments.of(Named.of(DropPrimaryKeyChange.class.getSimpleName(), dropPrimaryKeyChangeFactory())),
                Arguments.of(Named.of(DropProcedureChange.class.getSimpleName(), dropProcedureChangeFactory())),
                Arguments.of(Named.of(DropSequenceChange.class.getSimpleName(), dropSequenceChangeFactory())),
                Arguments.of(Named.of(DropTableChange.class.getSimpleName(), dropTableChangeFactory())),
                Arguments.of(
                    Named.of(DropUniqueConstraintChange.class.getSimpleName(), dropUniqueConstraintChangeFactory())
                ),
                Arguments.of(Named.of(DropViewChange.class.getSimpleName(), dropViewChangeFactory())),
                Arguments.of(Named.of(InsertDataChange.class.getSimpleName(), insertDataChangeFactory())),
                Arguments.of(Named.of(LoadDataChange.class.getSimpleName(), loadDataChangeFactory())),
                Arguments.of(Named.of(ModifyDataTypeChange.class.getSimpleName(), modifyDataTypeChangeFactory())),
                Arguments.of(Named.of(RenameSequenceChange.class.getSimpleName(), renameSequenceChangeFactory())),
                Arguments.of(Named.of(RenameTableChange.class.getSimpleName(), renameTableChangeFactory())),
                Arguments.of(Named.of(SetColumnRemarksChange.class.getSimpleName(), setColumnRemarksChangeFactory())),
                Arguments.of(Named.of(SetTableRemarksChange.class.getSimpleName(), setTableRemarksChangeFactory()))
            );
        }

        private Function<String, Change> addColumnChangeFactory() {
            return schemaName -> {
                AddColumnChange change = new AddColumnChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> addAutoIncrementChangeFactory() {
            return schemaName -> {
                AddAutoIncrementChange change = new AddAutoIncrementChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> addDefaultValueChangeFactory() {
            return schemaName -> {
                AddDefaultValueChange change = new AddDefaultValueChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> addForeignKeyConstraintChangeFactory() {
            return schemaName -> {
                AddForeignKeyConstraintChange change = new AddForeignKeyConstraintChange();
                change.setBaseTableSchemaName(schemaName);
                change.setReferencedTableSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> addLookupTableChangeFactory() {
            return schemaName -> {
                AddLookupTableChange change = new AddLookupTableChange();
                change.setExistingTableSchemaName(schemaName);
                change.setNewTableSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> addNotNullConstraintChangeFactory() {
            return schemaName -> {
                AddNotNullConstraintChange change = new AddNotNullConstraintChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> addPrimaryKeyChangeFactory() {
            return schemaName -> {
                AddPrimaryKeyChange change = new AddPrimaryKeyChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> alterSequenceChangeFactory() {
            return schemaName -> {
                AlterSequenceChange change = new AlterSequenceChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> addUniqueConstraintChangeFactory() {
            return schemaName -> {
                AddUniqueConstraintChange change = new AddUniqueConstraintChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> createTableChangeFactory() {
            return schemaName -> {
                CreateTableChange change = new CreateTableChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> mergeColumnChangeFactory() {
            return schemaName -> {
                MergeColumnChange change = new MergeColumnChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> renameColumnChangeFactory() {
            return schemaName -> {
                RenameColumnChange change = new RenameColumnChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> renameViewChangeFactory() {
            return schemaName -> {
                RenameViewChange change = new RenameViewChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> createViewChangeFactory() {
            return schemaName -> {
                CreateViewChange change = new CreateViewChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> createIndexChangeFactory() {
            return schemaName -> {
                CreateIndexChange change = new CreateIndexChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> createProcedureChangeFactory() {
            return schemaName -> {
                CreateProcedureChange change = new CreateProcedureChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> createSequenceChangeFactory() {
            return schemaName -> {
                CreateSequenceChange change = new CreateSequenceChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> deleteDataChangeFactory() {
            return schemaName -> {
                DeleteDataChange change = new DeleteDataChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> dropAllForeignKeyConstraintsChangeFactory() {
            return schemaName -> {
                DropAllForeignKeyConstraintsChange change = new DropAllForeignKeyConstraintsChange();
                change.setBaseTableSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> dropColumnChangeFactory() {
            return schemaName -> {
                DropColumnChange change = new DropColumnChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> dropDefaultValueChangeFactory() {
            return schemaName -> {
                DropDefaultValueChange change = new DropDefaultValueChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> dropForeignKeyConstraintChangeFactory() {
            return schemaName -> {
                DropForeignKeyConstraintChange change = new DropForeignKeyConstraintChange();
                change.setBaseTableSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> dropIndexChangeFactory() {
            return schemaName -> {
                DropIndexChange change = new DropIndexChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> dropNotNullConstraintChangeFactory() {
            return schemaName -> {
                DropNotNullConstraintChange change = new DropNotNullConstraintChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> dropPrimaryKeyChangeFactory() {
            return schemaName -> {
                DropPrimaryKeyChange change = new DropPrimaryKeyChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> dropProcedureChangeFactory() {
            return schemaName -> {
                DropProcedureChange change = new DropProcedureChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> dropSequenceChangeFactory() {
            return schemaName -> {
                DropSequenceChange change = new DropSequenceChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> dropTableChangeFactory() {
            return schemaName -> {
                DropTableChange change = new DropTableChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> dropUniqueConstraintChangeFactory() {
            return schemaName -> {
                DropUniqueConstraintChange change = new DropUniqueConstraintChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> dropViewChangeFactory() {
            return schemaName -> {
                DropViewChange change = new DropViewChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> insertDataChangeFactory() {
            return schemaName -> {
                InsertDataChange change = new InsertDataChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> loadDataChangeFactory() {
            return schemaName -> {
                LoadDataChange change = new LoadDataChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> modifyDataTypeChangeFactory() {
            return schemaName -> {
                ModifyDataTypeChange change = new ModifyDataTypeChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> renameSequenceChangeFactory() {
            return schemaName -> {
                RenameSequenceChange change = new RenameSequenceChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> renameTableChangeFactory() {
            return schemaName -> {
                RenameTableChange change = new RenameTableChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> setColumnRemarksChangeFactory() {
            return schemaName -> {
                SetColumnRemarksChange change = new SetColumnRemarksChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }

        private Function<String, Change> setTableRemarksChangeFactory() {
            return schemaName -> {
                SetTableRemarksChange change = new SetTableRemarksChange();
                change.setSchemaName(schemaName);
                return change;
            };
        }
    }
}
