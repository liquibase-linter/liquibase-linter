package io.github.liquibaselinter.integration;

class IllegalChangeTypesIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should not allow a illegal change type",
            "illegal-change-types/illegal-change-types.xml",
            "illegal-change-types/illegal-change-types.json",
            "Change type 'liquibase.change.core.LoadDataChange' is not allowed in this project"
        );

        shouldFail(
            "Should not allow a illegal change type simple",
            "illegal-change-types/illegal-change-types.xml",
            "illegal-change-types/illegal-change-types-simple.json",
            "Change type 'liquibase.change.core.LoadDataChange' is not allowed in this project"
        );
    }
}
