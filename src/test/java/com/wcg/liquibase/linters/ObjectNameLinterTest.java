package com.wcg.liquibase.linters;

import com.wcg.liquibase.config.rules.RuleRunner;
import com.wcg.liquibase.resolvers.AddColumnChangeParameterResolver;
import com.wcg.liquibase.resolvers.RuleRunnerParameterResolver;
import liquibase.change.core.AddColumnChange;
import liquibase.exception.ChangeLogParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({AddColumnChangeParameterResolver.class, RuleRunnerParameterResolver.class})
class ObjectNameLinterTest {

    private ObjectNameLinter objectNameLinter;

    @BeforeEach
    void setUp() {
        objectNameLinter = new ObjectNameLinter();
    }

    @DisplayName("Should only allow uppercase with _ separator")
    @Test
    void shouldOnlyAllowUppercaseWithUnderscoreSeparator(AddColumnChange addColumnChange, RuleRunner ruleRunner) throws ChangeLogParseException {
        final List<String> invalidNames = Arrays.asList("_TEST", "TEST_", "TE ST", "TeST");
        for (String invalidName : invalidNames) {
            ChangeLogParseException changeLogParseException =
                    assertThrows(ChangeLogParseException.class, () -> objectNameLinter.lintObjectName(invalidName, addColumnChange, ruleRunner));
            assertTrue(changeLogParseException.getMessage().contains("Object name '" + invalidName + "' name must be uppercase and use '_' separation"));
        }

        objectNameLinter.lintObjectName("VALID_NAME", addColumnChange, ruleRunner);
    }

    @DisplayName("Should only allow names under max length")
    @Test
    void shouldOnlyAllowNamesUnderMaxLength(AddColumnChange addColumnChange, RuleRunner ruleRunner) throws ChangeLogParseException {
        String tooLong = "TEST_TEST_TEST_TEST_TEST_TEST_TEST";
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> objectNameLinter.lintObjectName(tooLong, addColumnChange, ruleRunner));
        assertTrue(changeLogParseException.getMessage().contains("Object name '" + tooLong + "' must be less than 30 characters"));

        String notTooLong = "TEST_TEST_TEST_TEST_TEST_TEST";
        objectNameLinter.lintObjectName(notTooLong, addColumnChange, ruleRunner);
    }

    @DisplayName("Should catch when name is null, when trying to lint length")
    @Test
    void shouldCatchNullNamesWhenCheckingLength(AddColumnChange addColumnChange, RuleRunner ruleRunner) {
        ChangeLogParseException changeLogParseException =
                assertThrows(ChangeLogParseException.class, () -> objectNameLinter.lintObjectNameLength(null, addColumnChange, ruleRunner));
        assertTrue(changeLogParseException.getMessage().contains("Object name is null"));
    }

    @DisplayName("Should allow uppercase with, numbers and _ separator")
    @Test
    void shouldOnlyAllowUppercaseWithNumbersAndUnderscoreSeparator(AddColumnChange addColumnChange, RuleRunner ruleRunner) throws ChangeLogParseException {
        final List<String> invalidNames = Arrays.asList("_TEST", "TEST_", "TE ST", "TeST");
        for (String invalidName : invalidNames) {
            ChangeLogParseException changeLogParseException =
                    assertThrows(ChangeLogParseException.class, () -> objectNameLinter.lintObjectName(invalidName, addColumnChange, ruleRunner));
            assertTrue(changeLogParseException.getMessage().contains("Object name '" + invalidName + "' name must be uppercase and use '_' separation"));
        }
        objectNameLinter.lintObjectName("VALID_12_3NAME_99", addColumnChange, ruleRunner);
    }

    @DisplayName("Should allow uppercase with, numbers and _ separator")
    @Test
    void shouldNotAllowNullObjectName(AddColumnChange addColumnChange, RuleRunner ruleRunner) {
        final List<String> invalidNames = Collections.singletonList(null);
        for (String invalidName : invalidNames) {
            ChangeLogParseException changeLogParseException =
                    assertThrows(ChangeLogParseException.class, () -> objectNameLinter.lintObjectName(invalidName, addColumnChange, ruleRunner));
            assertTrue(changeLogParseException.getMessage().contains("Object name is null"));
        }
    }

}
