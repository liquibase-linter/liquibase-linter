package io.github.liquibaselinter.integration;

class ColumnNameIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldPass(
            "Should pass when column name matches the pattern",
            "column-name/column-name-rename-pass.xml",
            "column-name/lqlint.json"
        );

        shouldFail(
            "Should fail when column name does not match the pattern",
            "column-name/column-name-rename-fail.xml",
            "column-name/lqlint.json",
            "Column name 'INVALID_COLUMN_NAME' should be lower cased"
        );
    }
}
