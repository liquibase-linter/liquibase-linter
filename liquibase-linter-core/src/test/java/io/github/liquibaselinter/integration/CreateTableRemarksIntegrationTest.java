package io.github.liquibaselinter.integration;

class CreateTableRemarksIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when create table does not have populated remarks attribute",
            "create-table-remarks/create-table-remarks-fail.xml",
            "create-table-remarks/lqlint.json",
            "Create table must contain remark attribute"
        );

        shouldPass(
            "Should pass when create table has populated remarks attribute",
            "create-table-remarks/create-table-remarks-pass.xml",
            "create-table-remarks/lqlint.json"
        );
    }
}
