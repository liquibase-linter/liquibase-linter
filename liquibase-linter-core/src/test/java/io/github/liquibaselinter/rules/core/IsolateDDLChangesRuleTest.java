package io.github.liquibaselinter.rules.core;

import io.github.liquibaselinter.config.RuleConfig;
import io.github.liquibaselinter.resolvers.ChangeSetParameterResolver;
import io.github.liquibaselinter.rules.RuleViolation;
import liquibase.change.core.AddColumnChange;
import liquibase.change.core.CreateTableChange;
import liquibase.changelog.ChangeSet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ChangeSetParameterResolver.class)
class IsolateDDLChangesRuleTest {

    private final IsolateDDLChangesRule rule = new IsolateDDLChangesRule();

    @DisplayName("Should not allow more than one ddl change type in a single change set")
    @Test
    void shouldNotAllowMoreThanOneDDL(ChangeSet changeSet) {
        changeSet.addChange(new CreateTableChange());
        changeSet.addChange(new AddColumnChange());

        assertThat(rule.check(changeSet, RuleConfig.EMPTY))
            .extracting(RuleViolation::message)
            .containsExactly("Should only have a single ddl change per change set");
    }

    @DisplayName("Should allow one ddl change type in a change set")
    @Test
    void shouldAllowOneDDL(ChangeSet changeSet) {
        assertThat(rule.check(changeSet, RuleConfig.EMPTY)).isEmpty();
    }

}
