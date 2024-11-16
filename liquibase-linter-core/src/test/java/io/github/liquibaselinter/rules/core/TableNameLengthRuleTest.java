package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.function.Function;
import java.util.stream.Stream;
import liquibase.change.Change;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.RenameTableChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.assertj.core.api.Assertions.assertThat;

class TableNameLengthRuleTest {

    private final TableNameLengthRule rule = new TableNameLengthRule();

    @DisplayName("Table name must not exceed max length")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableNameMustNotExceedMaxLength(String changeName, Function<String, Change> changeFromTableName) {
        Change change = changeFromTableName.apply("TABLE");
        rule.configure(RuleConfig.builder().withMaxLength(4).build());

        assertThat(rule.invalid(change)).isTrue();
    }

    @DisplayName("Table name can equal max length")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableLengthCanEqualMaxLength(String changeName, Function<String, Change> changeFromTableName) {
        Change change = changeFromTableName.apply("TABLE");
        rule.configure(RuleConfig.builder().withMaxLength(5).build());

        assertThat(rule.invalid(change)).isFalse();
    }

    @DisplayName("Table name can be null")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableNameCanBeNull(String changeName, Function<String, Change> changeFromTableName) {
        Change change = changeFromTableName.apply(null);
        rule.configure(RuleConfig.builder().withMaxLength(4).build());

        assertThat(rule.invalid(change)).isFalse();
    }

    @DisplayName("Table name length rule should support formatted error message with length arg")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableNameLengthRuleShouldReturnFormattedErrorMessage(String changeName, Function<String, Change> changeFromTableName) {
        Change change = changeFromTableName.apply("TABLE_LONG");
        rule.configure(RuleConfig.builder().withMaxLength(5).withErrorMessage("Table '%s' name must not be longer than %d").build());

        assertThat(rule.getMessage(change)).isEqualTo("Table 'TABLE_LONG' name must not be longer than 5");
    }

    private static class ChangeFromTableNameArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of("CreateTableChange", createTableChangeFactory()),
                Arguments.of("RenameTableChange", renameTableChangeFactory())
            );
        }

        private Function<String, Change> createTableChangeFactory() {
            return tableName -> {
                CreateTableChange createTableChange = new CreateTableChange();
                createTableChange.setTableName(tableName);
                return createTableChange;
            };
        }

        private Function<String, Change> renameTableChangeFactory() {
            return tableName -> {
                RenameTableChange renameTableChange = new RenameTableChange();
                renameTableChange.setNewTableName(tableName);
                return renameTableChange;
            };
        }
    }
}
