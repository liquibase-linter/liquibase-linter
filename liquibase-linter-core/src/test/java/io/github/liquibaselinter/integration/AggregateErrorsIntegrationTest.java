package io.github.liquibaselinter.integration;

class AggregateErrorsIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should aggregate errors",
            "aggregate-errors/aggregate-errors.xml",
            "aggregate-errors/aggregate-errors.json",
            "Linting failed with 3 errors: \n - Change set must have a comment\n - Should have at least one context on the change set\n - Preconditions are not allowed in this project"
        );
    }
}
