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

class TableNameRuleTest {

    private final TableNameRule rule = new TableNameRule();

    @DisplayName("Table name must not be null")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableNameNameMustNotBeNull(Function<String, Change> changeFromTableName) {
        assertThat(rule).checkingChange(changeFromTableName.apply(null)).hasViolations();
    }

    @DisplayName("Table name must follow pattern")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableNameNameMustFollowPattern(Function<String, Change> changeFromTableName) {
        RuleConfig ruleConfig = RuleConfig.builder().withPattern("^(?!TBL)[A-Z_]+(?<!_)$").build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(changeFromTableName.apply("TBL_INVALID"))
            .hasExactlyViolationsMessages("Table name does not follow pattern");
        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(changeFromTableName.apply("TABLE_VALID"))
            .hasNoViolations();
    }

    @DisplayName("Table name rule should support formatted error message with pattern arg")
    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ChangeFromTableNameArgumentsProvider.class)
    void tableNameNameRuleShouldReturnFormattedErrorMessage(Function<String, Change> changeFromTableName) {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withPattern("^(?!TBL)[A-Z_]+(?<!_)$")
            .withErrorMessage("Table name '%s' must follow pattern '%s'")
            .build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(changeFromTableName.apply("TBL_INVALID"))
            .hasExactlyViolationsMessages("Table name 'TBL_INVALID' must follow pattern '^(?!TBL)[A-Z_]+(?<!_)$'");
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
