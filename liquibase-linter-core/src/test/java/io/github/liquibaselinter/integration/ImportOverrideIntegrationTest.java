package io.github.liquibaselinter.integration;

class ImportOverrideIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when object name only passes imported rules",
            "import-override/import-override-fail.xml",
            "import-override/lqlint.json",
            "Object name 'OBJ_COLUMN' name must be uppercase or numeric characters only"
        );

        shouldPass(
            "Should only use rule overrides",
            "import-override/import-override-pass.xml",
            "import-override/lqlint.json"
        );
    }
}
