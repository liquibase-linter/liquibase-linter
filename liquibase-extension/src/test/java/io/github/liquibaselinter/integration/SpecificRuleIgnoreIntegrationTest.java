package io.github.liquibaselinter.integration;

import io.github.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class SpecificRuleIgnoreIntegrationTest extends LinterIntegrationTest {
    // TODO test in more detail, proving that it still fails on other non-ignored rules, once #20 is fixed

    @Override
    void registerTests() {
        shouldPass(
            "Should be allowed to ignore specific rules",
            "specific-rule-ignore/specific-rule-ignore.xml",
            "specific-rule-ignore/specific-rule-ignore.json");
    }

}
