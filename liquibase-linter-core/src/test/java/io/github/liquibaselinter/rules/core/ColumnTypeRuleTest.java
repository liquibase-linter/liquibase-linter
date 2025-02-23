package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.function.Function;
import java.util.stream.Stream;
import liquibase.change.AddColumnConfig;
import liquibase.change.Change;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class ColumnTypeRuleTest {

    private final ColumnTypeRule rule = new ColumnTypeRule();

    @Test
    void shouldHaveName() {
        assertThat(rule).hasName("column-type");
    }

    @DisplayName("Column type must not be null")
    @ParameterizedTest
    @ArgumentsSource(ChangeFromColumnTypeArgumentsProvider.class)
    void columnTypeMustNotBeNull(Function<String, Change> changeFromColumnType) {
        Change change = changeFromColumnType.apply(null);

        assertThat(rule)
            .checkingChange(change)
            .hasExactlyViolationsMessages("Type '' of column 'column_name' does not follow pattern ''");
    }

    @DisplayName("Column type must follow pattern")
    @ParameterizedTest
    @ArgumentsSource(ChangeFromColumnTypeArgumentsProvider.class)
    void columnTypeMustFollowPattern(Function<String, Change> changeFromColumnType) {
        RuleConfig ruleConfig = RuleConfig.builder().withPattern("^varchar2\\(\\d+\\)$").build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(changeFromColumnType.apply("int"))
            .hasExactlyViolationsMessages(
                "Type 'int' of column 'column_name' does not follow pattern '^varchar2\\(\\d+\\)$'"
            );

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(changeFromColumnType.apply("varchar2(30)"))
            .hasNoViolations();
    }

    @DisplayName("Column name rule should support formatted error message with pattern arg")
    @ParameterizedTest
    @ArgumentsSource(ChangeFromColumnTypeArgumentsProvider.class)
    void columnTypeRuleShouldReturnFormattedErrorMessage(Function<String, Change> changeFromColumnType) {
        Change change = changeFromColumnType.apply("varchar");
        RuleConfig ruleConfig = RuleConfig.builder()
            .withPattern("^varchar2\\(\\d+\\)$")
            .withErrorMessage("Column type '%s' of column %s must follow pattern '%s'")
            .build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(change)
            .hasExactlyViolationsMessages(
                "Column type 'varchar' of column column_name must follow pattern '^varchar2\\(\\d+\\)$'"
            );
    }

    private static class ChangeFromColumnTypeArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of(Named.of("AddColumnChange", addColumnChangeFactory())),
                Arguments.of(Named.of("CreateTable", createTableChangeFactory()))
            );
        }

        private Function<String, Change> createTableChangeFactory() {
            return columnType -> {
                AddColumnConfig columnConfig = new AddColumnConfig();
                columnConfig.setName("column_name");
                columnConfig.setType(columnType);
                CreateTableChange createTableChange = new CreateTableChange();
                createTableChange.addColumn(columnConfig);
                return createTableChange;
            };
        }

        private Function<String, Change> addColumnChangeFactory() {
            return columnType -> {
                AddColumnConfig columnConfig = new AddColumnConfig();
                columnConfig.setName("column_name");
                columnConfig.setType(columnType);
                AddColumnChange addColumnChange = new AddColumnChange();
                addColumnChange.addColumn(columnConfig);
                return addColumnChange;
            };
        }
    }
}
