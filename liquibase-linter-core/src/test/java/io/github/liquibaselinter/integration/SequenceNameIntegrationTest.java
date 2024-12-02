package io.github.liquibaselinter.integration;

class SequenceNameIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when sequence name does not match the pattern",
            "sequence-name/sequence-name-fail.xml",
            "sequence-name/lqlint.json",
            "Sequence 'SEQ_TEST' name must be uppercase, use '_' separation and not start with SEQ");

        shouldPass(
            "Should pass when sequence name matches the pattern",
            "sequence-name/sequence-name-pass.xml",
            "sequence-name/lqlint.json");

        shouldFail(
            "Should fail when sequence name does not match the pattern",
            "sequence-name/sequence-name-rename-fail.xml",
            "sequence-name/lqlint.json",
            "Sequence 'SEQ_TEST' name must be uppercase, use '_' separation and not start with SEQ");
    }

}
