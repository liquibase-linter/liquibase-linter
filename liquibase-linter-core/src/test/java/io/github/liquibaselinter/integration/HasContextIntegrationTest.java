package io.github.liquibaselinter.integration;

class HasContextIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should not pass with no context value",
            "has-context/has-context-fail.xml",
            "has-context/has-context.json",
            "Should have at least one context on the change set");

        shouldPass(
            "Should pass with a context value",
            "has-context/has-context-pass.xml",
            "has-context/has-context.json");
    }

}
