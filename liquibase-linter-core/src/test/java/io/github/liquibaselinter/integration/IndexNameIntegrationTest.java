package io.github.liquibaselinter.integration;

class IndexNameIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when create index name does not match the pattern",
            "index-name/create-index-name-fail.xml",
            "index-name/lqlint.json",
            "Index 'TABLE_NAME' must follow pattern table name followed by 'I' and a number e.g. APPLICATION_I1, or match a primary key or unique constraint name"
        );

        shouldPass(
            "Should pass when create index name matches the pattern",
            "index-name/create-index-name-pass.xml",
            "index-name/lqlint.json"
        );

        shouldPass(
            "Should pass when create index name does not match the pattern but the condition is not met",
            "index-name/create-index-name-pass-condition-not-met.xml",
            "index-name/lqlint.json"
        );
    }
}
