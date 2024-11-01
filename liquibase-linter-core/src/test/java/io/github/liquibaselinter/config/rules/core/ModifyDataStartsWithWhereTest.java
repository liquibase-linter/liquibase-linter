package io.github.liquibaselinter.config.rules.core;

import io.github.liquibaselinter.resolvers.ChangeSetParameterResolver;
import liquibase.change.core.DeleteDataChange;
import liquibase.change.core.UpdateDataChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(ChangeSetParameterResolver.class)
class ModifyDataStartsWithWhereTest {

    @Test
    void shouldNotAllowWhereConditionToStartWithWhereCaseInsensitive(ChangeSet changeSet) {
        ModifyDataStartsWithWhere modifyDataStartsWithWhere = new ModifyDataStartsWithWhere();
        assertTrue(modifyDataStartsWithWhere.invalid(getUpdateDataChange(changeSet, "WHERE table = 'X'")));
        assertTrue(modifyDataStartsWithWhere.invalid(getDeleteDataChange(changeSet, "WHERE table = 'X'")));

        assertTrue(modifyDataStartsWithWhere.invalid(getUpdateDataChange(changeSet, "where table = 'X'")));
        assertTrue(modifyDataStartsWithWhere.invalid(getDeleteDataChange(changeSet, "where table = 'X'")));
    }

    @Test
    void shouldBeValidOnNullWhereValue(ChangeSet changeSet) {
        ModifyDataStartsWithWhere modifyDataStartsWithWhere = new ModifyDataStartsWithWhere();
        assertFalse(modifyDataStartsWithWhere.invalid(getUpdateDataChange(changeSet, null)));
        assertFalse(modifyDataStartsWithWhere.invalid(getDeleteDataChange(changeSet, null)));
    }

    private UpdateDataChange getUpdateDataChange(ChangeSet changeSet, String where) {
        UpdateDataChange updateDataChange = new UpdateDataChange();
        updateDataChange.setTableName("TABLE");
        updateDataChange.setWhere(where);
        updateDataChange.setChangeSet(changeSet);
        return updateDataChange;
    }

    private DeleteDataChange getDeleteDataChange(ChangeSet changeSet, String where) {
        DeleteDataChange deleteDataChange = new DeleteDataChange();
        deleteDataChange.setTableName("TABLE");
        deleteDataChange.setWhere(where);
        deleteDataChange.setChangeSet(changeSet);
        return deleteDataChange;
    }

}
