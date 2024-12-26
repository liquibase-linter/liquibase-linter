package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.resolvers.ChangeSetParameterResolver;
import liquibase.change.core.DropNotNullConstraintChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith({ ChangeSetParameterResolver.class })
class DropNotNullRequireColumnDataTypeRuleTest {

    private final DropNotNullRequireColumnDataTypeRule rule = new DropNotNullRequireColumnDataTypeRule();

    @DisplayName("Should allow non null column data type")
    @Test
    void shouldAllowNonNullColumnDataType(ChangeSet changeSet) {
        assertThat(rule).checkingChange(build(changeSet, "NVARCHAR(10)")).hasNoViolations();
    }

    @DisplayName("Should not allow null column data type")
    @Test
    void shouldNotAllowNullColumnDataType(ChangeSet changeSet) {
        assertThat(rule)
            .checkingChange(build(changeSet, null))
            .hasExactlyViolationsMessages("Drop not null constraint column data type attribute must be populated");
    }

    @DisplayName("Should not allow blank column data type")
    @Test
    void shouldNotAllowBlankColumnDataType(ChangeSet changeSet) {
        assertThat(rule)
            .checkingChange(build(changeSet, ""))
            .hasExactlyViolationsMessages("Drop not null constraint column data type attribute must be populated");
    }

    private DropNotNullConstraintChange build(ChangeSet changeSet, String columnDataType) {
        DropNotNullConstraintChange change = new DropNotNullConstraintChange();
        change.setColumnDataType(columnDataType);
        change.setChangeSet(changeSet);
        return change;
    }
}
