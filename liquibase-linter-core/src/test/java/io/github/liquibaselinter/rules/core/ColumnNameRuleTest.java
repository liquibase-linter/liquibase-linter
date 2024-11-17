package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.function.Function;
import java.util.stream.Stream;
import liquibase.change.AddColumnConfig;
import liquibase.change.Change;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.MergeColumnChange;
import liquibase.change.core.RenameColumnChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class ColumnNameRuleTest {

    private final ColumnNameRule rule = new ColumnNameRule();

    @Test
    void shouldHaveName() {
        assertThat(rule).hasName("column-name");
    }

    @DisplayName("Column name must not be null")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromColumnNameArgumentsProvider.class)
    void columnNameNameMustNotBeNull(String changeName, Function<String, Change> changeFromColumnName) {
        Change change = changeFromColumnName.apply(null);

        assertThat(rule)
            .checkingChange(change)
            .hasExactlyViolationsMessages("Column name '' does not follow pattern ''");
    }

    @DisplayName("Column name must follow pattern")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromColumnNameArgumentsProvider.class)
    void columnNameNameMustFollowPattern(String changeName, Function<String, Change> changeFromColumnName) {
        RuleConfig ruleConfig = RuleConfig.builder().withPattern("^(?!COL)[A-Z_]+(?<!_)$").build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(changeFromColumnName.apply("COL_INVALID"))
            .hasExactlyViolationsMessages("Column name 'COL_INVALID' does not follow pattern '^(?!COL)[A-Z_]+(?<!_)$'");

        assertThat(rule).withConfig(ruleConfig).checkingChange(changeFromColumnName.apply("VALID")).hasNoViolations();
    }

    @DisplayName("Column name rule should support formatted error message with pattern arg")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromColumnNameArgumentsProvider.class)
    void columnNameNameRuleShouldReturnFormattedErrorMessage(
        String changeName,
        Function<String, Change> changeFromColumnName
    ) {
        Change change = changeFromColumnName.apply("COL_INVALID");
        RuleConfig ruleConfig = RuleConfig.builder()
            .withPattern("^(?!COL)[A-Z_]+(?<!_)$")
            .withErrorMessage("Column name '%s' must follow pattern '%s'")
            .build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(change)
            .hasExactlyViolationsMessages("Column name 'COL_INVALID' must follow pattern '^(?!COL)[A-Z_]+(?<!_)$'");
    }

    private static class ChangeFromColumnNameArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of("AddColumnChange", addColumnChangeFactory()),
                Arguments.of("RenameColumnChange", renameColumnChangeFactory()),
                Arguments.of("CreateTable", createTableChangeFactory()),
                Arguments.of("MergeColumnChange", mergeColumnChangeFactory())
            );
        }

        private Function<String, Change> createTableChangeFactory() {
            return columnName -> {
                AddColumnConfig columnConfig = new AddColumnConfig();
                columnConfig.setName(columnName);
                CreateTableChange createTableChange = new CreateTableChange();
                createTableChange.addColumn(columnConfig);
                return createTableChange;
            };
        }

        private Function<String, Change> addColumnChangeFactory() {
            return columnName -> {
                AddColumnConfig columnConfig = new AddColumnConfig();
                columnConfig.setName(columnName);
                AddColumnChange addColumnChange = new AddColumnChange();
                addColumnChange.addColumn(columnConfig);
                return addColumnChange;
            };
        }

        private Function<String, Change> renameColumnChangeFactory() {
            return columnName -> {
                RenameColumnChange renameColumnChange = new RenameColumnChange();
                renameColumnChange.setNewColumnName(columnName);
                return renameColumnChange;
            };
        }

        private Function<String, Change> mergeColumnChangeFactory() {
            return columnName -> {
                MergeColumnChange mergeColumnChange = new MergeColumnChange();
                mergeColumnChange.setFinalColumnName(columnName);
                return mergeColumnChange;
            };
        }
    }
}
