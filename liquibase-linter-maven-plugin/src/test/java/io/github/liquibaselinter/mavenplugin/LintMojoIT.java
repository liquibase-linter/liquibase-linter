package io.github.liquibaselinter.mavenplugin;


import com.soebes.itf.jupiter.extension.MavenGoal;
import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.maven.MavenExecutionResult;

import static com.soebes.itf.extension.assertj.MavenExecutionResultAssert.assertThat;

@MavenJupiterExtension
class LintMojoIT {

    @MavenTest
    @MavenGoal("test")
    void detect_lint_issue_with_configuration_using_relative_path(MavenExecutionResult result) {
        assertThatLintIssueIsDetectedAndReported(result);
    }

    @MavenTest
    @MavenGoal("test")
    void detect_lint_issue_with_configuration_using_absolute_path(MavenExecutionResult result) {
        assertThatLintIssueIsDetectedAndReported(result);
    }

    @MavenTest
    @MavenGoal("test")
    void detect_lint_issue_with_configuration_using_jsonc(MavenExecutionResult result) {
        assertThatLintIssueIsDetectedAndReported(result);
    }

    @MavenTest
    @MavenGoal("test")
    void detect_lint_issue_with_default_configuration_file(MavenExecutionResult result) {
        assertThatLintIssueIsDetectedAndReported(result);
    }

    private static void assertThatLintIssueIsDetectedAndReported(MavenExecutionResult result) {
        assertThat(result).isFailure();

        assertThat(result)
            .out()
            .plain()
            .containsSequence(
                "[ERROR] 1 violation(s) found",
                "[ERROR] src/main/resources/config/liquibase/master.xml",
                "[ERROR] \tchangeSet '2'",
                "[ERROR] \t\t'index-name': Index names must be the table name, suffixed with 'IX' and a two-digit number, e.g. FOO_IX01"
            );

        assertThat(result)
            .err()
            .plain()
            .isEmpty();
    }
}
