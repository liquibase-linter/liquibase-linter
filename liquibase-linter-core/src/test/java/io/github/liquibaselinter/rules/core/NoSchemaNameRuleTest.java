package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.rules.core.SchemaNameRules.NoSchemaNameRule;
import java.util.function.Function;
import java.util.stream.Stream;
import liquibase.change.Change;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.AddForeignKeyConstraintChange;
import liquibase.change.core.AddPrimaryKeyChange;
import liquibase.change.core.AddUniqueConstraintChange;
import liquibase.change.core.CreateIndexChange;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.CreateViewChange;
import liquibase.change.core.MergeColumnChange;
import liquibase.change.core.RenameColumnChange;
import liquibase.change.core.RenameViewChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class NoSchemaNameRuleTest {

    private final NoSchemaNameRule rule = new NoSchemaNameRule();

    @DisplayName("Schema name should be null")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeWithSchemaNameArgumentsProvider.class)
    void definedSchemaNameShouldBeInvalid(Function<String, Change> changeWithSchemaNameFactory) {
        assertThat(rule)
            .checkingChange(changeWithSchemaNameFactory.apply("SCHEMA_NAME"))
            .hasExactlyViolationsMessages("Schema names are not allowed in this project");
    }

    @DisplayName("Schema name null should be valid")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeWithSchemaNameArgumentsProvider.class)
    void schemaNameNullShouldBeValid(Function<String, Change> changeWithSchemaNameFactory) {
        assertThat(rule).checkingChange(changeWithSchemaNameFactory.apply(null)).hasNoViolations();
    }

    @DisplayName("Schema name empty should be valid")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeWithSchemaNameArgumentsProvider.class)
    void schemaNameEmptyShouldBeValid(Function<String, Change> changeWithSchemaNameFactory) {
        assertThat(rule).checkingChange(changeWithSchemaNameFactory.apply("")).hasNoViolations();
    }

    private static class ChangeWithSchemaNameArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of(Named.of(AddColumnChange.class.getSimpleName(), addColumnChangeFactory())),
                Arguments.of(
                    Named.of(
                        AddForeignKeyConstraintChange.class.getSimpleName(),
                        addForeignKeyConstraintChangeFactory()
                    )
                ),
                Arguments.of(Named.of(AddPrimaryKeyChange.class.getSimpleName(), addPrimaryKeyChangeFactory())),
                Arguments.of(
                    Named.of(AddUniqueConstraintChange.class.getSimpleName(), addUniqueConstraintChangeFactory())
                ),
                Arguments.of(Named.of(CreateTableChange.class.getSimpleName(), createTableChangeFactory())),
                Arguments.of(Named.of(MergeColumnChange.class.getSimpleName(), mergeColumnChangeFactory())),
                Arguments.of(Named.of(RenameColumnChange.class.getSimpleName(), renameColumnChangeFactory())),
                Arguments.of(Named.of(RenameViewChange.class.getSimpleName(), renameViewChangeFactory())),
                Arguments.of(Named.of(CreateViewChange.class.getSimpleName(), createViewChangeFactory())),
                Arguments.of(Named.of(CreateIndexChange.class.getSimpleName(), createIndexChangeFactory()))
            );
        }

        private Function<String, Change> addColumnChangeFactory() {
            return schemaName -> {
                AddColumnChange addColumnChange = new AddColumnChange();
                addColumnChange.setSchemaName(schemaName);
                return addColumnChange;
            };
        }

        private Function<String, Change> addForeignKeyConstraintChangeFactory() {
            return schemaName -> {
                AddForeignKeyConstraintChange addForeignKeyConstraintChange = new AddForeignKeyConstraintChange();
                addForeignKeyConstraintChange.setBaseTableSchemaName(schemaName);
                addForeignKeyConstraintChange.setReferencedTableSchemaName(schemaName);
                return addForeignKeyConstraintChange;
            };
        }

        private Function<String, Change> addPrimaryKeyChangeFactory() {
            return schemaName -> {
                AddPrimaryKeyChange addPrimaryKeyChange = new AddPrimaryKeyChange();
                addPrimaryKeyChange.setSchemaName(schemaName);
                return addPrimaryKeyChange;
            };
        }

        private Function<String, Change> addUniqueConstraintChangeFactory() {
            return schemaName -> {
                AddUniqueConstraintChange addUniqueConstraintChange = new AddUniqueConstraintChange();
                addUniqueConstraintChange.setSchemaName(schemaName);
                return addUniqueConstraintChange;
            };
        }

        private Function<String, Change> createTableChangeFactory() {
            return schemaName -> {
                CreateTableChange createTableChange = new CreateTableChange();
                createTableChange.setSchemaName(schemaName);
                return createTableChange;
            };
        }

        private Function<String, Change> mergeColumnChangeFactory() {
            return schemaName -> {
                MergeColumnChange mergeColumnChange = new MergeColumnChange();
                mergeColumnChange.setSchemaName(schemaName);
                return mergeColumnChange;
            };
        }

        private Function<String, Change> renameColumnChangeFactory() {
            return schemaName -> {
                RenameColumnChange renameColumnChange = new RenameColumnChange();
                renameColumnChange.setSchemaName(schemaName);
                return renameColumnChange;
            };
        }

        private Function<String, Change> renameViewChangeFactory() {
            return schemaName -> {
                RenameViewChange renameViewChange = new RenameViewChange();
                renameViewChange.setSchemaName(schemaName);
                return renameViewChange;
            };
        }

        private Function<String, Change> createViewChangeFactory() {
            return schemaName -> {
                CreateViewChange createViewChange = new CreateViewChange();
                createViewChange.setSchemaName(schemaName);
                return createViewChange;
            };
        }

        private Function<String, Change> createIndexChangeFactory() {
            return schemaName -> {
                CreateIndexChange createViewChange = new CreateIndexChange();
                createViewChange.setSchemaName(schemaName);
                return createViewChange;
            };
        }
    }
}
