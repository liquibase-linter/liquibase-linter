package io.github.liquibaselinter.integration;

class HasCommentIntegrationTest extends LinterIntegrationTest {

    @Override
    void registerTests() {
        shouldFail(
            "Should fail with no comment xml",
            "has-comment/has-comment-fail.xml",
            "has-comment/lqlint.json",
            "Change set must have a comment"
        );

        shouldPass("Should pass with a comment xml", "has-comment/has-comment-pass.xml", "has-comment/lqlint.json");

        shouldPass(
            "Should pass with a tagDatabase change and no comment",
            "has-comment/has-comment-tagdatabase-pass.xml",
            "has-comment/lqlint.json"
        );

        shouldFail(
            "Should fail with no comment json",
            "has-comment/has-comment-fail.json",
            "has-comment/lqlint.json",
            "Change set must have a comment"
        );

        shouldPass("Should pass with a comment json", "has-comment/has-comment-pass.xml", "has-comment/lqlint.json");

        shouldFail(
            "Should fail with no comment yaml",
            "has-comment/has-comment-fail.yaml",
            "has-comment/lqlint.json",
            "Change set must have a comment"
        );

        shouldPass("Should pass with a comment yaml", "has-comment/has-comment-pass.yaml", "has-comment/lqlint.json");
    }
}
