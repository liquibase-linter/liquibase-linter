package io.github.liquibaselinter.config.rules.core;

import io.github.liquibaselinter.resolvers.ChangeSetParameterResolver;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ChangeSetParameterResolver.class)
class IsolateDDLChangesRuleTest {

    @DisplayName("Should not allow more than one ddl change type in a single change set")
    @Test
    void shouldNotAllowMoreThanOneDDL(ChangeSet changeSet) {
        changeSet.addChange(new CreateTableChange());
        changeSet.addChange(new AddColumnChange());
        assertTrue(new IsolateDDLChangesRule().invalid(changeSet));
    }

    @DisplayName("Should allow one ddl change type in a change set")
    @Test
    void shouldAllowOneDDL(ChangeSet changeSet) {
        changeSet.addChange(new CreateTableChange());
        assertFalse(new IsolateDDLChangesRule().invalid(changeSet));
    }

}
