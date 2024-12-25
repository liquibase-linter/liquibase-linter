package io.github.liquibaselinter.rules.core;

import static io.github.liquibaselinter.rules.ChangeRuleAssert.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.core.CreateIndexChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IndexTablespaceRuleTest {

    private final IndexTablespaceRule rule = new IndexTablespaceRule();

    @Test
    void shouldHaveName() {
        assertThat(rule).hasName("index-tablespace");
    }

    @Test
    @DisplayName("Tablespace should not be empty")
    void indexTablespaceShouldNotBeEmpty() {
        assertThat(rule)
            .checkingChange(createIndexWithTablespace(null))
            .hasExactlyViolationsMessages("Tablespace '' of index 'idx_foo' is empty or does not follow pattern ''");
    }

    @Test
    @DisplayName("Tablespace must follow pattern")
    void indexTablespaceMustFollowPattern() {
        RuleConfig ruleConfig = RuleConfig.builder().withPattern("^TAB_IDX_[A-Z_]+$").build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(createIndexWithTablespace("INDEXES"))
            .hasExactlyViolationsMessages(
                "Tablespace 'INDEXES' of index 'idx_foo' is empty or does not follow pattern '^TAB_IDX_[A-Z_]+$'"
            );

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(createIndexWithTablespace("TAB_IDX_USERS"))
            .hasNoViolations();
    }

    @DisplayName("Should support formatted error message with pattern arg")
    @Test
    void indexTablespaceRuleShouldReturnFormattedErrorMessage() {
        RuleConfig ruleConfig = RuleConfig.builder()
            .withPattern("^TAB_IDX_[A-Z_]+$")
            .withErrorMessage("Tablespace '%s' for index '%s' must follow pattern '%s'")
            .build();

        assertThat(rule)
            .withConfig(ruleConfig)
            .checkingChange(createIndexWithTablespace("DDD-001"))
            .hasExactlyViolationsMessages(
                "Tablespace 'DDD-001' for index 'idx_foo' must follow pattern '^TAB_IDX_[A-Z_]+$'"
            );
    }

    private CreateIndexChange createIndexWithTablespace(String tablespace) {
        CreateIndexChange createIndexChange = new CreateIndexChange();
        createIndexChange.setIndexName("idx_foo");
        createIndexChange.setTablespace(tablespace);
        return createIndexChange;
    }
}
