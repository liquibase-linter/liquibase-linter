package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.resolvers.ChangeSetParameterResolver;
import io.github.liquibaselinter.rules.core.ModifyDataStartsWithWhere;
import liquibase.change.core.DeleteDataChange;
import liquibase.change.core.UpdateDataChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ChangeSetParameterResolver.class)
class ModifyDataStartsWithWhereTest {

    @Test
    void shouldNotAllowWhereConditionToStartWithWhereCaseInsensitive(ChangeSet changeSet) {
        ModifyDataStartsWithWhere modifyDataStartsWithWhere = new ModifyDataStartsWithWhere();
        assertThat(modifyDataStartsWithWhere.invalid(getUpdateDataChange(changeSet, "WHERE table = 'X'"))).isTrue();
        assertThat(modifyDataStartsWithWhere.invalid(getDeleteDataChange(changeSet, "WHERE table = 'X'"))).isTrue();

        assertThat(modifyDataStartsWithWhere.invalid(getUpdateDataChange(changeSet, "where table = 'X'"))).isTrue();
        assertThat(modifyDataStartsWithWhere.invalid(getDeleteDataChange(changeSet, "where table = 'X'"))).isTrue();
    }

    @Test
    void shouldBeValidOnNullWhereValue(ChangeSet changeSet) {
        ModifyDataStartsWithWhere modifyDataStartsWithWhere = new ModifyDataStartsWithWhere();
        assertThat(modifyDataStartsWithWhere.invalid(getUpdateDataChange(changeSet, null))).isFalse();
        assertThat(modifyDataStartsWithWhere.invalid(getDeleteDataChange(changeSet, null))).isFalse();
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
