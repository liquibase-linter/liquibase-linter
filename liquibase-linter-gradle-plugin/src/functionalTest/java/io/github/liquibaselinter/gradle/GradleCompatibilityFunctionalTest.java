package io.github.liquibaselinter.gradle;

import static io.github.liquibaselinter.gradle.TestProject.CREATE_TABLE_CHANGESET;
import static io.github.liquibaselinter.gradle.TestProject.INVALID_INDEX_CHANGESET;
import static io.github.liquibaselinter.gradle.TestProject.LINT_CONFIGURATION;
import static io.github.liquibaselinter.gradle.TestProject.changeLog;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Checks the plugin against older Gradle versions than the one running the build.
 */
class GradleCompatibilityFunctionalTest {

    @TempDir
    Path projectDir;

    static Stream<String> gradleVersions() {
        return Arrays.stream(System.getProperty("liquibaseLinter.compatibilityGradleVersions", "").split(","))
            .map(String::trim)
            .filter(version -> !version.isEmpty());
    }

    @ParameterizedTest(name = "Gradle {0}")
    @MethodSource("gradleVersions")
    void detects_lint_issue(String gradleVersion) {
        TestProject project = new TestProject(projectDir)
            .withGradleVersion(gradleVersion)
            .buildScript(
                """
                plugins {
                    java
                    id("io.github.liquibase-linter")
                }

                liquibaseLinter {
                    changeLogFile = "src/main/resources/master.xml"
                }
                """
            )
            .file("src/test/resources/lqlint.json", LINT_CONFIGURATION)
            .file("src/main/resources/master.xml", changeLog(CREATE_TABLE_CHANGESET, INVALID_INDEX_CHANGESET));

        LiquibaseLinterPluginFunctionalTest.assertLintIssueIsReported(
            project.buildAndFail("check", "--configuration-cache"),
            "src/main/resources/master.xml"
        );
    }
}
