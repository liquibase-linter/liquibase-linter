package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.rules.RuleViolation;
import liquibase.changelog.DatabaseChangeLog;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChangeLogFileNameRuleTest {

    private final ChangeLogFileNameRule rule = new ChangeLogFileNameRule();

    @Test
    @DisplayName("ChangeLog filename must follow pattern")
    void changeLogFileNameMustFollowPattern() {
        RuleConfig ruleConfig = RuleConfig.builder().withPattern("^[^ ]+$").build();

        assertThat(rule.check(changeLogWithFilePath("//test/dir/this has spaces.xml"), ruleConfig))
            .extracting(RuleViolation::message)
            .containsExactly("ChangeLog filename '//test/dir/this has spaces.xml' must follow pattern '^[^ ]+$'");

        assertThat(rule.check(changeLogWithFilePath("//test/dir/this-has-spaces.xml"), ruleConfig)).isEmpty();
    }

    @DisplayName("ChangeLog filename rule should support formatted error message with pattern arg")
    @Test
    void changeLogFileNameRuleShouldReturnFormattedErrorMessage() {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withPattern("^\\d{8}_[a-z_]+$")
            .withErrorMessage("ChangeLog has a filename '%s' that doesn't match pattern '%s'")
            .build();

        assertThat(rule.check(changeLogWithFilePath("001_create_table_foo"), ruleConfig))
            .extracting(RuleViolation::message)
            .containsExactly(
                "ChangeLog has a filename '001_create_table_foo' that doesn't match pattern '^\\d{8}_[a-z_]+$'"
            );
    }

    private DatabaseChangeLog changeLogWithFilePath(String filePath) {
        final DatabaseChangeLog databaseChangeLog = new DatabaseChangeLog();
        databaseChangeLog.setPhysicalFilePath(filePath);
        return databaseChangeLog;
    }
}
