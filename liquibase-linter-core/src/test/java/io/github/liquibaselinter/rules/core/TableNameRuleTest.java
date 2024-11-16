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

class TableNameRuleTest {

    private final TableNameRule rule = new TableNameRule();

    @DisplayName("Table name must not be null")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableNameNameMustNotBeNull(String changeName, Function<String, Change> changeFromTableName) {
        Change change = changeFromTableName.apply(null);

        assertThat(rule.invalid(change)).isTrue();
    }

    @DisplayName("Table name must follow pattern")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableNameNameMustFollowPattern(String changeName, Function<String, Change> changeFromTableName) {
        rule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").build());

        assertThat(rule.invalid(changeFromTableName.apply("TBL_INVALID"))).isTrue();
        assertThat(rule.invalid(changeFromTableName.apply("TABLE_VALID"))).isFalse();
    }

    @DisplayName("Table name rule should support formatted error message with pattern arg")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableNameNameRuleShouldReturnFormattedErrorMessage(String changeName, Function<String, Change> changeFromTableName) {
        Change change = changeFromTableName.apply("TBL_INVALID");
        rule.configure(RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").withErrorMessage("Table name '%s' must follow pattern '%s'").build());

        assertThat(rule.getMessage(change)).isEqualTo("Table name 'TBL_INVALID' must follow pattern '^(?!TBL)[A-Z_]+(?<!_)$'");
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
