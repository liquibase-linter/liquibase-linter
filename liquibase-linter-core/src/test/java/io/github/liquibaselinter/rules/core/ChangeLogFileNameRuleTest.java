package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.changelog.DatabaseChangeLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChangeLogFileNameRuleTest {

    private final ChangeLogFileNameRule changeLogFileNameRule = new ChangeLogFileNameRule();

    @Test
    @DisplayName("ChangeLog filename must follow pattern")
    void changeLogFileNameMustFollowPattern() {
        changeLogFileNameRule.configure(RuleConfig.builder().withPattern("^[^ ]+$").build());

        assertThat(changeLogFileNameRule.invalid(changeLogWithFilePath("//test/dir/this has spaces.xml"))).isTrue();
        assertThat(changeLogFileNameRule.invalid(changeLogWithFilePath("//test/dir/this-has-spaces.xml"))).isFalse();
    }

    @DisplayName("ChangeLog filename rule should support formatted error message with pattern arg")
    @Test
    void changeLogFileNameRuleShouldReturnFormattedErrorMessage() {
        changeLogFileNameRule.configure(RuleConfig.builder().withPattern("^\\d{8}_[a-z_]+$").withErrorMessage("ChangeLog has a filename '%s' that doesn't match pattern '%s'").build());
        assertThat(changeLogFileNameRule.getMessage(changeLogWithFilePath("001_create_table_foo"))).isEqualTo("ChangeLog has a filename '001_create_table_foo' that doesn't match pattern '^\\d{8}_[a-z_]+$'");
    }

    private DatabaseChangeLog changeLogWithFilePath(String filePath) {
        final DatabaseChangeLog databaseChangeLog = new DatabaseChangeLog();
        databaseChangeLog.setPhysicalFilePath(filePath);
        return databaseChangeLog;
    }
}
