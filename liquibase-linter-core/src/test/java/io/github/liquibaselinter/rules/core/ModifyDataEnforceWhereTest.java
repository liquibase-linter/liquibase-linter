package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.resolvers.ChangeSetParameterResolver;
import io.github.liquibaselinter.rules.ChangeRuleAssert;
import liquibase.change.core.DeleteDataChange;
import liquibase.change.core.UpdateDataChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ChangeSetParameterResolver.class)
class ModifyDataEnforceWhereTest {

    private final ModifyDataEnforceWhere rule = new ModifyDataEnforceWhere();

    @Test
    void shouldEnforceWhereConditionOnCertainTablesNullValue(ChangeSet changeSet) {
        RuleConfig ruleConfig = RuleConfig.builder().withValues("TABLE_NAME").build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getUpdateDataChange(changeSet, null))
            .hasExactlyViolationsMessages("Modify data on table 'TABLE_NAME' must have a where condition");

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getDeleteDataChange(changeSet, null))
            .hasExactlyViolationsMessages("Modify data on table 'TABLE_NAME' must have a where condition");
    }

    @Test
    void shouldEnforceWhereConditionOnCertainTablesEmptyValue(ChangeSet changeSet) {
        RuleConfig ruleConfig = RuleConfig.builder().withValues("TABLE_NAME").build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getUpdateDataChange(changeSet, ""))
            .hasExactlyViolationsMessages("Modify data on table 'TABLE_NAME' must have a where condition");

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getDeleteDataChange(changeSet, ""))
            .hasExactlyViolationsMessages("Modify data on table 'TABLE_NAME' must have a where condition");
    }

    @Test
    void shouldEnforcePresenceAndPattern(ChangeSet changeSet) {
        RuleConfig ruleConfig = RuleConfig.builder().withValues("TABLE_NAME").withPattern("^.*CODE =.*$").build();

        ChangeRuleAssert ruleAssertion = assertThat(rule).withConfig(ruleConfig);
        ruleAssertion.checkingChange(getUpdateDataChange(changeSet, null)).hasViolations();
        ruleAssertion.checkingChange(getUpdateDataChange(changeSet, "sausages")).hasViolations();
        ruleAssertion.checkingChange(getUpdateDataChange(changeSet, "CODE = 'foo'")).hasNoViolations();
    }

    @DisplayName("Modify data change should support formatter error messages")
    @Test
    void foreignKeyNameRuleShouldReturnFormattedErrorMessage(ChangeSet changeSet) {
        RuleConfig ruleConfig = RuleConfig.builder().withErrorMessage("Custom error message for table '%s'").build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getUpdateDataChange(changeSet, null))
            .hasExactlyViolationsMessages("Custom error message for table 'TABLE_NAME'");

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(getDeleteDataChange(changeSet, null))
            .hasExactlyViolationsMessages("Custom error message for table 'TABLE_NAME'");
    }

    private static UpdateDataChange getUpdateDataChange(ChangeSet changeSet, String where) {
        UpdateDataChange updateDataChange = new UpdateDataChange();
        updateDataChange.setTableName("TABLE_NAME");
        updateDataChange.setWhere(where);
        updateDataChange.setChangeSet(changeSet);
        return updateDataChange;
    }

    private static DeleteDataChange getDeleteDataChange(ChangeSet changeSet, String where) {
        DeleteDataChange deleteDataChange = new DeleteDataChange();
        deleteDataChange.setTableName("TABLE_NAME");
        deleteDataChange.setWhere(where);
        deleteDataChange.setChangeSet(changeSet);
        return deleteDataChange;
    }
}
