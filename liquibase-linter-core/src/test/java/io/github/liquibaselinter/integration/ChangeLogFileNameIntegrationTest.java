package io.github.liquibaselinter.integration;

class ChangeLogFileNameIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should not allow file name with spaces",
            "changelog-file-name/file-name with-space.xml",
            "changelog-file-name/changelog-file-name.json",
            "changelog-file-name/file-name with-space.xml -- Message: ChangeLog filename 'changelog-file-name/file-name with-space.xml' must follow pattern '^[^ ]+$'"
        );

        shouldFail(
            "Should not allow included file with name that has spaces",
            "changelog-file-name/file-name-no-spaces.xml",
            "changelog-file-name/changelog-file-name.json",
            "changelog-file-name/file-name with-space.xml -- Message: ChangeLog filename 'changelog-file-name/file-name with-space.xml' must follow pattern '^[^ ]+$'"
        );

        shouldPass(
            "Should allow file name without spaces",
            "changelog-file-name/file-name-no-spaces-valid.xml",
            "changelog-file-name/changelog-file-name.json"
        );
    }
}
