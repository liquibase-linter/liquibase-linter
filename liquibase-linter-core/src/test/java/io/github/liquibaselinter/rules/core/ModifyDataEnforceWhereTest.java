package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.resolvers.ChangeSetParameterResolver;
import liquibase.change.core.DeleteDataChange;
import liquibase.change.core.UpdateDataChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ChangeSetParameterResolver.class)
class ModifyDataEnforceWhereTest {

    @Test
    void shouldEnforceWhereConditionOnCertainTablesNullValue(ChangeSet changeSet) {
        final ModifyDataEnforceWhere modifyDataEnforceWhere = new ModifyDataEnforceWhere();
        modifyDataEnforceWhere.configure(RuleConfig.builder().withValues(Collections.singletonList("REQUIRES_WHERE")).build());
        assertThat(modifyDataEnforceWhere.invalid(getUpdateDataChange(changeSet, null))).isTrue();
        assertThat(modifyDataEnforceWhere.invalid(getDeleteDataChange(changeSet, null))).isTrue();
    }

    @Test
    void shouldEnforceWhereConditionOnCertainTablesEmptyValue(ChangeSet changeSet) {
        final ModifyDataEnforceWhere modifyDataEnforceWhere = new ModifyDataEnforceWhere();
        modifyDataEnforceWhere.configure(RuleConfig.builder().withValues(Collections.singletonList("REQUIRES_WHERE")).build());
        assertThat(modifyDataEnforceWhere.invalid(getUpdateDataChange(changeSet, ""))).isTrue();
        assertThat(modifyDataEnforceWhere.invalid(getDeleteDataChange(changeSet, ""))).isTrue();
    }

    @Test
    void shouldEnforcePresenceAndPattern(ChangeSet changeSet) {
        ModifyDataEnforceWhere rule = new ModifyDataEnforceWhere();
        rule.configure(RuleConfig.builder()
            .withValues(Collections.singletonList("REQUIRES_WHERE"))
            .withPattern("^.*CODE =.*$")
            .build());

        assertThat(rule.invalid(getUpdateDataChange(changeSet, null))).isTrue();
        assertThat(rule.invalid(getUpdateDataChange(changeSet, "sausages"))).isTrue();
        assertThat(rule.invalid(getUpdateDataChange(changeSet, "CODE = 'foo'"))).isFalse();
    }

    @DisplayName("Modify data change should support formatter error messages")
    @Test
    void foreignKeyNameRuleShouldReturnFormattedErrorMessage(ChangeSet changeSet) {
        ModifyDataEnforceWhere rule = new ModifyDataEnforceWhere();
        rule.configure(RuleConfig.builder().build());
        assertThat(rule.getMessage(getUpdateDataChange(changeSet, null))).isEqualTo("Modify data on table 'REQUIRES_WHERE' must have a where condition");
        assertThat(rule.getMessage(getDeleteDataChange(changeSet, null))).isEqualTo("Modify data on table 'REQUIRES_WHERE' must have a where condition");
    }

    private UpdateDataChange getUpdateDataChange(ChangeSet changeSet, String where) {
        UpdateDataChange updateDataChange = new UpdateDataChange();
        updateDataChange.setTableName("REQUIRES_WHERE");
        updateDataChange.setWhere(where);
        updateDataChange.setChangeSet(changeSet);
        return updateDataChange;
    }

    private DeleteDataChange getDeleteDataChange(ChangeSet changeSet, String where) {
        DeleteDataChange deleteDataChange = new DeleteDataChange();
        deleteDataChange.setTableName("REQUIRES_WHERE");
        deleteDataChange.setWhere(where);
        deleteDataChange.setChangeSet(changeSet);
        return deleteDataChange;
    }

}
