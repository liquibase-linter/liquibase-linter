package com.whiteclarkegroup.liquibaselinter.integration;

import com.whiteclarkegroup.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(LiquibaseIntegrationTestResolver.class)
class FileNotIncludedIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should not allow file not included in deltas change log",
            "file-not-included/file-not-included-root.xml",
            "file-not-included/file-not-included.json",
            "Changelog files not included in deltas change log: integration/file-not-included/to-include/test-included-2.xml");

        shouldPass(
            "Should allow all files included in deltas change log",
            "file-not-included/files-included-root.xml",
            "file-not-included/file-not-included.json");
    }

}
