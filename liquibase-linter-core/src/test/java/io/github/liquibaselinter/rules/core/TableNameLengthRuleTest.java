package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import java.util.function.Function;
import java.util.stream.Stream;
import liquibase.change.Change;
import liquibase.change.core.CreateTableChange;
import liquibase.change.core.RenameTableChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class TableNameLengthRuleTest {

    private final TableNameLengthRule rule = new TableNameLengthRule();

    @DisplayName("Table name must not exceed max length")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableNameMustNotExceedMaxLength(Function<String, Change> changeFromTableName) {
        RuleConfig ruleConfig = RuleConfig.builder().withMaxLength(4).build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(changeFromTableName.apply("TABLE"))
            .hasExactlyViolationsMessages("Table 'TABLE' name must not be longer than 4");
    }

    @DisplayName("Table name can equal max length")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableLengthCanEqualMaxLength(Function<String, Change> changeFromTableName) {
        RuleConfig ruleConfig = RuleConfig.builder().withMaxLength(5).build();

        assertThat(rule).withConfig(ruleConfig).checkingChange(changeFromTableName.apply("TABLE")).hasNoViolations();
    }

    @DisplayName("Table name can be null")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableNameCanBeNull(Function<String, Change> changeFromTableName) {
        RuleConfig ruleConfig = RuleConfig.builder().withMaxLength(4).build();

        assertThat(rule).withConfig(ruleConfig).checkingChange(changeFromTableName.apply(null)).hasNoViolations();
    }

    @DisplayName("Table name length rule should support formatted error message with length arg")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableNameLengthRuleShouldReturnFormattedErrorMessage(Function<String, Change> changeFromTableName) {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withMaxLength(5)
            .withErrorMessage("Table '%s' name must not be longer than %d")
            .build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(changeFromTableName.apply("TABLE_LONG"))
            .hasExactlyViolationsMessages("Table 'TABLE_LONG' name must not be longer than 5");
    }

    private static class ChangeFromTableNameArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of(Named.of(CreateTableChange.class.getSimpleName(), createTableChangeFactory())),
                Arguments.of(Named.of(RenameTableChange.class.getSimpleName(), renameTableChangeFactory()))
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
