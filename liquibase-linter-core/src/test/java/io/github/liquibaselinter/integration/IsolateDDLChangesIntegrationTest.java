package io.github.liquibaselinter.integration;

class IsolateDDLChangesIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail with more than one ddl change within a single change set",
            "isolate-ddl-changes/isolate-ddl-changes-fail.xml",
            "isolate-ddl-changes/lqlint.json",
            "Should only have a single ddl change per change set"
        );

        shouldPass(
            "Should pass with a single ddl change within a single change set",
            "isolate-ddl-changes/isolate-ddl-changes-pass.xml",
            "isolate-ddl-changes/lqlint.json"
        );
    }
}
