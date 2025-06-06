package io.github.liquibaselinter.integration;

class PrimaryKeyNameIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when the table name can't be enforced but the suffix isn't used",
            "primary-key-name/primary-key-name-fail-on-suffix.xml",
            "primary-key-name/primary-key-name-complex.json",
            "Primary key constraint 'NOT_EVEN_CLOSE' must be named, ending with '_PK', and start with table name (unless too long)"
        );

        shouldFail(
            "Should fail when the table name can be enforced and isn't used",
            "primary-key-name/primary-key-name-fail-on-tablename.xml",
            "primary-key-name/primary-key-name-complex.json",
            "Primary key constraint 'BAZ_PK' must be named, ending with '_PK', and start with table name (unless too long)"
        );

        shouldFail(
            "Should fail when the table name can be enforced and isn't used",
            "primary-key-name/primary-key-name-fail-on-create-table-tablename.xml",
            "primary-key-name/primary-key-name-complex.json",
            "Primary key constraint 'BAZ_PK' must be named, ending with '_PK', and start with table name (unless too long)"
        );

        shouldPass(
            "Should pass when used correctly",
            "primary-key-name/primary-key-name-pass.xml",
            "primary-key-name/primary-key-name-complex.json"
        );

        shouldFail(
            "Should fail when omitted with simple config",
            "primary-key-name/primary-key-name-fail-omitted.xml",
            "primary-key-name/primary-key-name-simple.json",
            "Primary key name '' is missing or does not follow pattern"
        );

        shouldFail(
            "Should fail when omitted with complex config",
            "primary-key-name/primary-key-name-fail-omitted.xml",
            "primary-key-name/primary-key-name-complex.json",
            "Primary key constraint '' must be named, ending with '_PK', and start with table name (unless too long)"
        );
    }
}
