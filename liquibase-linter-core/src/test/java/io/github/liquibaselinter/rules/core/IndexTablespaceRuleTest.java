package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.change.core.CreateIndexChange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IndexTablespaceRuleTest {

    private final IndexTablespaceRule rule = new IndexTablespaceRule();

    @Test
    void shouldHaveName() {
        assertThat(rule.getName()).isEqualTo("index-tablespace");
    }

    @Test
    @DisplayName("Tablespace should not be empty")
    void indexTablespaceShouldNotBeEmpty() {
        rule.configure(RuleConfig.EMPTY);

        assertThat(rule.invalid(createIndexWithTablespace(null))).isTrue();
        assertThat(rule.getMessage(createIndexWithTablespace(null))).isEqualTo(
            "Tablespace '' of index 'idx_foo' is empty or does not follow pattern ''"
        );
    }

    @Test
    @DisplayName("Tablespace must follow pattern")
    void indexTablespaceMustFollowPattern() {
        rule.configure(RuleConfig.builder().withPattern("^TAB_IDX_[A-Z_]+$").build());

        assertThat(rule.invalid(createIndexWithTablespace("INDEXES"))).isTrue();
        assertThat(rule.getMessage(createIndexWithTablespace("INDEXES"))).isEqualTo(
            "Tablespace 'INDEXES' of index 'idx_foo' is empty or does not follow pattern '^TAB_IDX_[A-Z_]+$'"
        );

        assertThat(rule.invalid(createIndexWithTablespace("TAB_IDX_USERS"))).isFalse();
    }

    @DisplayName("Should support formatted error message with pattern arg")
    @Test
    void indexTablespaceRuleShouldReturnFormattedErrorMessage() {
        rule.configure(
            RuleConfig.builder()
                .withPattern("^TAB_IDX_[A-Z_]+$")
                .withErrorMessage("Tablespace '%s' for index '%s' must follow pattern '%s'")
                .build()
        );

        assertThat(rule.getMessage(createIndexWithTablespace("DDD-001"))).isEqualTo(
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
