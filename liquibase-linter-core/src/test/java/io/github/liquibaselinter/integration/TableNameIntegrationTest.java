package io.github.liquibaselinter.integration;

class TableNameIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when table name does not match the pattern",
            "table-name/table-name-fail.xml",
            "table-name/lqlint.json",
            "Table 'TBL_TEST' name must be uppercase, use '_' separation and not start with TBL");

        shouldPass(
            "Should pass when table name matches the pattern",
            "table-name/table-name-pass.xml",
            "table-name/lqlint.json");

        shouldFail(
            "Should fail when table name does not match the pattern",
            "table-name/table-name-rename-fail.xml",
            "table-name/lqlint.json",
            "Table 'TBL_TEST' name must be uppercase, use '_' separation and not start with TBL");
    }

}
