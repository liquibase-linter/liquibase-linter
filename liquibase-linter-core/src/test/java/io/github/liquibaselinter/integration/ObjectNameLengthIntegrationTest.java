package io.github.liquibaselinter.integration;

class ObjectNameLengthIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail when object name length is exceeded",
            "object-name-length/object-name-length-fail.xml",
            "object-name-length/lqlint.json",
            "Object name 'THIS_OBJECT_NAME_IS_FAR_TOO_LONG' must be less than 30 characters");

        shouldPass(
            "Should pass when object name length is not exceeded",
            "object-name-length/object-name-length-pass.xml",
            "object-name-length/lqlint.json");
    }

}
