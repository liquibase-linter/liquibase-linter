package io.github.liquibaselinter.integration;

class CreateColumnNullableConstraintIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when create column does not have a constraints tag with a populated nullable attribute",
            "create-column-nullable-constraint/create-column-nullable-constraint-fail.xml",
            "create-column-nullable-constraint/lqlint.json",
            "Add column must specify nullable constraint"
        );

        shouldPass(
            "Should pass when create column has a constraints tag with a populated nullable attribute",
            "create-column-nullable-constraint/create-column-nullable-constraint-pass.xml",
            "create-column-nullable-constraint/lqlint.json"
        );

        shouldFail(
            "Should fail when create table column does not have a constraints tag with a populated nullable attribute",
            "create-column-nullable-constraint/create-column-nullable-constraint-fail-create-table.xml",
            "create-column-nullable-constraint/lqlint.json",
            "Add column must specify nullable constraint"
        );
    }
}
