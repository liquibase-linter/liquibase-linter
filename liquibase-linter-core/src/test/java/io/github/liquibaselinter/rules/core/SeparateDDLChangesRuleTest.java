package io.github.liquibaselinter.rules.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import io.github.liquibaselinter.config.RuleConfig;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.Test;

class SeparateDDLChangesRuleTest {

    private final SeparateDDLChangesRule rule = new SeparateDDLChangesRule();

    @Test
    void shouldPassWhenChangeSetHasNoContextFilter() {
        ChangeSet changeSet = mock(ChangeSet.class);
        RuleConfig config = RuleConfig.builder().withPattern("^NEVER_MATCHES$").build();

        assertThat(rule.check(changeSet, config)).isEmpty();
    }
}
