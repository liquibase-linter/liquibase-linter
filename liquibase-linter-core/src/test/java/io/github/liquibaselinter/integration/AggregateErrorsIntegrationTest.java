package io.github.liquibaselinter.integration;

class AggregateErrorsIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should aggregate errors",
            "aggregate-errors/aggregate-errors.xml",
            "aggregate-errors/aggregate-errors.json",
            "Linting failed with 3 errors"
        );
    }

}
