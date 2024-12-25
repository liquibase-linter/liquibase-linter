package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import java.util.function.Function;
import java.util.stream.Stream;
import liquibase.change.Change;
import liquibase.change.core.DeleteDataChange;
import liquibase.change.core.UpdateDataChange;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class ModifyDataStartsWithWhereTest {

    private final ModifyDataStartsWithWhere rule = new ModifyDataStartsWithWhere();

    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ModifyDataWithWhereArgumentsProvider.class)
    void shouldNotAllowWhereConditionToStartWithWhereCaseInsensitive(
        Function<String, Change> modifyDataChangeWithWhereFactory
    ) {
        assertThat(rule)
            .checkingChange(modifyDataChangeWithWhereFactory.apply("WHERE table = 'X'"))
            .hasExactlyViolationsMessages("Modify data where starts with where clause, that's probably a mistake");

        assertThat(rule)
            .checkingChange(modifyDataChangeWithWhereFactory.apply("where table = 'X'"))
            .hasExactlyViolationsMessages("Modify data where starts with where clause, that's probably a mistake");
    }

    @ParameterizedTest(name = "With {0}")
    @ArgumentsSource(ModifyDataWithWhereArgumentsProvider.class)
    void shouldBeValidOnNullWhereValue(Function<String, Change> modifyDataChangeWithWhereFactory) {
        assertThat(rule).checkingChange(modifyDataChangeWithWhereFactory.apply(null)).hasNoViolations();
    }

    private static class ModifyDataWithWhereArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                Arguments.of(Named.of(UpdateDataChange.class.getSimpleName(), updateDataChangeFactory())),
                Arguments.of(Named.of(DeleteDataChange.class.getSimpleName(), deleteDataChangeFactory()))
            );
        }

        private Function<String, Change> updateDataChangeFactory() {
            return where -> {
                UpdateDataChange updateDataChange = new UpdateDataChange();
                updateDataChange.setTableName("TABLE");
                updateDataChange.setWhere(where);
                return updateDataChange;
            };
        }

        private Function<String, Change> deleteDataChangeFactory() {
            return where -> {
                DeleteDataChange deleteDataChange = new DeleteDataChange();
                deleteDataChange.setTableName("TABLE");
                deleteDataChange.setWhere(where);
                return deleteDataChange;
            };
        }
    }
}
