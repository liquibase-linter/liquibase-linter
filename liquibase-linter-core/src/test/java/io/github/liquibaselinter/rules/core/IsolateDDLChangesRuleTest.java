package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.resolvers.ChangeSetParameterResolver;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ChangeSetParameterResolver.class)
class IsolateDDLChangesRuleTest {

    @DisplayName("Should not allow more than one ddl change type in a single change set")
    @Test
    void shouldNotAllowMoreThanOneDDL(ChangeSet changeSet) {
        changeSet.addChange(new CreateTableChange());
        changeSet.addChange(new AddColumnChange());
        assertThat(new IsolateDDLChangesRule().invalid(changeSet)).isTrue();
    }

    @DisplayName("Should allow one ddl change type in a change set")
    @Test
    void shouldAllowOneDDL(ChangeSet changeSet) {
        changeSet.addChange(new CreateTableChange());
        assertThat(new IsolateDDLChangesRule().invalid(changeSet)).isFalse();
    }

}
