package io.github.liquibaselinter.integration;

class CreateColumnRemarksIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when create column does not have populated remarks attribute",
            "create-column-remarks/create-column-remarks-fail.xml",
            "create-column-remarks/lqlint.json",
            "Add column 'TEST_COLUMN' must contain remarks"
        );

        shouldPass(
            "Should pass when create column has populated remarks attribute",
            "create-column-remarks/create-column-remarks-pass.xml",
            "create-column-remarks/lqlint.json"
        );

        shouldFail(
            "Should fail when create column does not have populated remarks attribute",
            "create-column-remarks/create-column-remarks-create-table-fail.xml",
            "create-column-remarks/lqlint.json",
            "Add column 'TEST_COLUMN' must contain remarks"
        );
    }
}
