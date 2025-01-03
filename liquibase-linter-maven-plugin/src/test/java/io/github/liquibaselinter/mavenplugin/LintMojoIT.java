package io.github.liquibaselinter.mavenplugin;

import static com.soebes.itf.extension.assertj.MavenExecutionResultAssert.assertThat;

import com.soebes.itf.jupiter.extension.MavenGoal;
import com.soebes.itf.jupiter.extension.MavenJupiterExtension;
import com.soebes.itf.jupiter.extension.MavenTest;
import com.soebes.itf.jupiter.maven.MavenExecutionResult;

@MavenJupiterExtension
class LintMojoIT {

    @MavenTest
    @MavenGoal("test")
    void detect_lint_issue_with_configuration_using_relative_path(MavenExecutionResult result) {
        assertThatLintIssueIsDetectedAndReported(result, "src/main/resources/config/liquibase/master.xml");
    }

    @MavenTest
    @MavenGoal("test")
    void detect_lint_issue_with_configuration_using_absolute_path(MavenExecutionResult result) {
        assertThatLintIssueIsDetectedAndReported(result, "src/main/resources/config/liquibase/master.xml");
    }

    @MavenTest
    @MavenGoal("test")
    void detect_lint_issue_with_configuration_using_jsonc(MavenExecutionResult result) {
        assertThatLintIssueIsDetectedAndReported(result, "src/main/resources/config/liquibase/master.xml");
    }

    @MavenTest
    @MavenGoal("test")
    void detect_lint_issue_with_default_configuration_file(MavenExecutionResult result) {
        assertThatLintIssueIsDetectedAndReported(result, "src/main/resources/config/liquibase/master.xml");
    }

    @MavenTest
    @MavenGoal("test")
    void detect_and_parse_all_changelogs(MavenExecutionResult result) {
        assertThatLintIssueIsDetectedAndReported(result, "config/liquibase/changelog/001_create_table_example.xml");
    }

    private static void assertThatLintIssueIsDetectedAndReported(MavenExecutionResult result, String faultyChangelog) {
        assertThat(result).isFailure();

        assertThat(result)
            .out()
            .plain()
            .containsSequence(
                "[ERROR] 1 violation(s) found",
                "[ERROR] " + faultyChangelog,
                "[ERROR] \tchangeSet '2'",
                "[ERROR] \t\t'index-name': Index names must be the table name, suffixed with 'IX' and a two-digit number, e.g. FOO_IX01"
            );

        assertThat(result).err().plain().isEmpty();
    }
}
