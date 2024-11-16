package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.resolvers.ChangeSetParameterResolver;
import io.github.liquibaselinter.rules.core.DropNotNullRequireColumnDataTypeRule;
import liquibase.change.core.DropNotNullConstraintChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith({ChangeSetParameterResolver.class})
class DropNotNullRequireColumnDataTypeRuleTest {

    private DropNotNullRequireColumnDataTypeRule dropNotNullRequireColumnDataTypeRule;

    @BeforeEach
    void setUp() {
        dropNotNullRequireColumnDataTypeRule = new DropNotNullRequireColumnDataTypeRule();
    }

    @DisplayName("Should allow non null column data type")
    @Test
    void shouldAllowNonNullColumnDataType(ChangeSet changeSet) {
        assertThat(dropNotNullRequireColumnDataTypeRule.invalid(build(changeSet, "NVARCHAR(10)"))).isFalse();
    }

    @DisplayName("Should not allow null column data type")
    @Test
    void shouldNotAllowNullColumnDataType(ChangeSet changeSet) {
        assertThat(dropNotNullRequireColumnDataTypeRule.invalid(build(changeSet, null))).isTrue();
    }

    @DisplayName("Should not allow blank column data type")
    @Test
    void shouldNotAllowBlankColumnDataType(ChangeSet changeSet) {
        assertThat(dropNotNullRequireColumnDataTypeRule.invalid(build(changeSet, ""))).isTrue();
    }

    private DropNotNullConstraintChange build(ChangeSet changeSet, String columnDataType) {
        DropNotNullConstraintChange change = new DropNotNullConstraintChange();
        change.setColumnDataType(columnDataType);
        change.setChangeSet(changeSet);
        return change;
    }

}
