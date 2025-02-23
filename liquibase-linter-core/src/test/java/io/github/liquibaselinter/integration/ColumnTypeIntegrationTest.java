package io.github.liquibaselinter.integration;

class ColumnTypeIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldPass(
            "Should pass when column name matches the pattern",
            "column-type/add-column-pass.xml",
            "column-type/lqlint.json"
        );

        shouldFail(
            "Should fail when column name does not match the pattern",
            "column-type/add-column-fail.xml",
            "column-type/lqlint.json",
            "Type 'varchar2(50)' of column 'reference' does not follow pattern '^varchar2\\(30\\)$"
        );

        shouldFail(
            "Should fail when column name does not match the pattern",
            "column-type/add-column-fail.xml",
            "column-type/lqlint.json",
            "Type 'uuid' of column 'id' does not follow pattern '^int$"
        );
    }
}
