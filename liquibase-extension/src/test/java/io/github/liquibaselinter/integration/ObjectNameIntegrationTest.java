package io.github.liquibaselinter.integration;

import io.github.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class ObjectNameIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when object name does not match the pattern",
            "object-name/object-name-fail.xml",
            "object-name/lqlint.json",
            "Object name 'NOT%FOLLOWING%PATTERN' name must be uppercase and use '_' separation");

        shouldPass(
            "Should pass when object name matches the pattern",
            "object-name/object-name-pass.xml",
            "object-name/lqlint.json");
    }

}
