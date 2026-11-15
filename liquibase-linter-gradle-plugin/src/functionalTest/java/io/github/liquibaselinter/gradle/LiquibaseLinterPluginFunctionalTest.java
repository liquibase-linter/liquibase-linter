package io.github.liquibaselinter.gradle;

import static io.github.liquibaselinter.gradle.TestProject.CREATE_TABLE_CHANGESET;
import static io.github.liquibaselinter.gradle.TestProject.INDEX_NAME_VIOLATION;
import static io.github.liquibaselinter.gradle.TestProject.INVALID_INDEX_CHANGESET;
import static io.github.liquibaselinter.gradle.TestProject.LINT_CONFIGURATION;
import static io.github.liquibaselinter.gradle.TestProject.VALID_INDEX_CHANGESET;
import static io.github.liquibaselinter.gradle.TestProject.changeLog;
import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LiquibaseLinterPluginFunctionalTest {

    private static final String TASK = ":liquibaseLinterCheck";
    private static final String MASTER = "src/main/resources/db/changelog/master.xml";

    @TempDir
    Path projectDir;

    private TestProject project() {
        return new TestProject(projectDir);
    }

    private TestProject javaProject(String extraConfiguration) {
        return project()
            .buildScript(
                """
                plugins {
                    java
                    id("io.github.liquibase-linter")
                }

                liquibaseLinter {
                    changeLogFile = "%s"
                    %s
                }
                """.formatted(MASTER, extraConfiguration)
            )
            .file("src/test/resources/lqlint.json", LINT_CONFIGURATION);
    }

    @Test
    void detects_lint_issue_with_default_configuration_file() {
        TestProject project = javaProject("").file(MASTER, changeLog(CREATE_TABLE_CHANGESET, INVALID_INDEX_CHANGESET));

        BuildResult result = project.buildAndFail("check");

        assertThat(result.task(TASK).getOutcome()).isEqualTo(TaskOutcome.FAILED);
        assertLintIssueIsReported(result, MASTER);
        assertThat(result.getOutput()).contains("Linting failed with 1 errors", "See the report at: file:");
        assertThat(project.path("build/reports/liquibase-linter/liquibaseLinterCheck.txt"))
            .content()
            .contains(MASTER, "changeSet '2'", INDEX_NAME_VIOLATION);
    }

    @Test
    void passes_without_violation_and_writes_the_report() {
        TestProject project = javaProject("").file(MASTER, changeLog(CREATE_TABLE_CHANGESET, VALID_INDEX_CHANGESET));

        BuildResult result = project.build("check");

        assertThat(result.task(TASK).getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
        assertThat(result.getOutput()).doesNotContain("violation(s) found");
        assertThat(project.path("build/reports/liquibase-linter/liquibaseLinterCheck.txt"))
            .content()
            .contains("Summary:", "ERROR: 0");
    }

    @Test
    void detects_lint_issue_with_configuration_using_relative_path() {
        TestProject project = javaProject("configurationFile = file(\"config/lint/lqlint.json\")")
            .file("config/lint/lqlint.json", LINT_CONFIGURATION)
            .file(MASTER, changeLog(CREATE_TABLE_CHANGESET, INVALID_INDEX_CHANGESET));

        assertLintIssueIsReported(project.buildAndFail("check"), MASTER);
    }

    @Test
    void detects_lint_issue_with_configuration_outside_of_project_directory(@TempDir Path configDir) {
        TestProject configProject = new TestProject(configDir).file("lqlint.json", LINT_CONFIGURATION);
        String configPath = configProject.path("lqlint.json").toAbsolutePath().toString().replace('\\', '/');
        TestProject project = javaProject("configurationFile = file(\"" + configPath + "\")").file(
            MASTER,
            changeLog(CREATE_TABLE_CHANGESET, INVALID_INDEX_CHANGESET)
        );

        assertLintIssueIsReported(project.buildAndFail("check"), MASTER);
    }

    @Test
    void detects_lint_issue_with_configuration_using_jsonc() {
        TestProject project = javaProject("configurationFile = file(\"src/test/resources/lqlint.jsonc\")")
            .file("src/test/resources/lqlint.jsonc", "// Comments are allowed in JSONC\n" + LINT_CONFIGURATION)
            .file(MASTER, changeLog(CREATE_TABLE_CHANGESET, INVALID_INDEX_CHANGESET));

        assertLintIssueIsReported(project.buildAndFail("check"), MASTER);
    }

    @Test
    void detects_lint_issue_in_included_changelogs() {
        TestProject project = javaProject("")
            .file(
                MASTER,
                """
                <databaseChangeLog
                  xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd"
                >
                  <include file="db/changelog/changes/001_create_table.xml" relativeToChangelogFile="false" />
                  <include file="classpath:db/changelog/changes/002_create_index.xml" />
                  <include file="changes/003_create_other_index.xml" relativeToChangelogFile="true" />
                </databaseChangeLog>
                """
            )
            .file("src/main/resources/db/changelog/changes/001_create_table.xml", changeLog(CREATE_TABLE_CHANGESET))
            .file("src/main/resources/db/changelog/changes/002_create_index.xml", changeLog(INVALID_INDEX_CHANGESET))
            .file(
                "src/main/resources/db/changelog/changes/003_create_other_index.xml",
                changeLog(VALID_INDEX_CHANGESET.replace("id=\"2\"", "id=\"3\""))
            );

        assertLintIssueIsReported(project.buildAndFail("check"), "db/changelog/changes/002_create_index.xml");
    }

    @Test
    void detects_lint_issue_with_changelog_from_classpath() {
        TestProject project = project()
            .buildScript(
                """
                plugins {
                    java
                    id("io.github.liquibase-linter")
                }

                liquibaseLinter {
                    changeLogFile = "classpath:db/changelog/master.xml"
                }
                """
            )
            .file("src/test/resources/lqlint.json", LINT_CONFIGURATION)
            .file(MASTER, changeLog(CREATE_TABLE_CHANGESET, INVALID_INDEX_CHANGESET));

        BuildResult result = project.buildAndFail("check");

        assertThat(result.getOutput()).contains("1 violation(s) found", INDEX_NAME_VIOLATION);
    }

    @Test
    void detects_lint_issue_in_changeset_restricted_to_another_dbms() {
        TestProject project = javaProject("").file(
            MASTER,
            changeLog(
                CREATE_TABLE_CHANGESET,
                INVALID_INDEX_CHANGESET.replace("id=\"2\"", "id=\"2\" dbms=\"postgresql\"")
            )
        );

        assertLintIssueIsReported(project.buildAndFail("check"), MASTER);
    }

    @Test
    void works_without_the_java_plugin() {
        TestProject project = project()
            .buildScript(
                """
                plugins {
                    id("io.github.liquibase-linter")
                }

                liquibaseLinter {
                    changeLogFile = "db/master.xml"
                }
                """
            )
            .file("src/test/resources/lqlint.json", LINT_CONFIGURATION)
            .file("db/master.xml", changeLog(CREATE_TABLE_CHANGESET, INVALID_INDEX_CHANGESET));

        assertLintIssueIsReported(project.buildAndFail("check"), "db/master.xml");
    }

    @Test
    void does_not_fail_the_build_when_failures_are_ignored() {
        TestProject project = javaProject("ignoreFailures = true").file(
            MASTER,
            changeLog(CREATE_TABLE_CHANGESET, INVALID_INDEX_CHANGESET)
        );

        BuildResult result = project.build("check");

        assertThat(result.task(TASK).getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
        assertLintIssueIsReported(result, MASTER);
    }

    @Test
    void skips_lint_with_configuration() {
        TestProject project = javaProject("skip = true").file(MASTER, changeLog(INVALID_INDEX_CHANGESET));

        BuildResult result = project.build("check");

        assertThat(result.task(TASK).getOutcome()).isEqualTo(TaskOutcome.SKIPPED);
    }

    @Test
    void skips_lint_with_gradle_property() {
        TestProject project = javaProject("").file(MASTER, changeLog(INVALID_INDEX_CHANGESET));

        BuildResult result = project.build("check", "-Pliquibase-linter.skip=true");

        assertThat(result.task(TASK).getOutcome()).isEqualTo(TaskOutcome.SKIPPED);
    }

    @Test
    void supports_additional_tasks() {
        TestProject project = project()
            .buildScript(
                """
                import io.github.liquibaselinter.gradle.LiquibaseLinterCheck

                plugins {
                    java
                    id("io.github.liquibase-linter")
                }

                liquibaseLinter {
                    changeLogFile = "%s"
                }

                val checkLegacyChangeLog = tasks.register<LiquibaseLinterCheck>("checkLegacyChangeLog") {
                    changeLogFile = "src/main/resources/db/legacy.xml"
                }
                tasks.check {
                    dependsOn(checkLegacyChangeLog)
                }
                """.formatted(MASTER)
            )
            .file("src/test/resources/lqlint.json", LINT_CONFIGURATION)
            .file(MASTER, changeLog(CREATE_TABLE_CHANGESET, VALID_INDEX_CHANGESET))
            .file("src/main/resources/db/legacy.xml", changeLog(CREATE_TABLE_CHANGESET, INVALID_INDEX_CHANGESET));

        BuildResult result = project.buildAndFail("check", "--continue");

        assertThat(result.task(TASK).getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
        assertThat(result.task(":checkLegacyChangeLog").getOutcome()).isEqualTo(TaskOutcome.FAILED);
        assertLintIssueIsReported(result, "src/main/resources/db/legacy.xml");
        assertThat(project.path("build/reports/liquibase-linter/checkLegacyChangeLog.txt")).exists();
    }

    @Test
    void is_up_to_date_until_a_changelog_changes() {
        TestProject project = javaProject("").file(MASTER, changeLog(CREATE_TABLE_CHANGESET, VALID_INDEX_CHANGESET));

        assertThat(project.build(TASK).task(TASK).getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
        assertThat(project.build(TASK).task(TASK).getOutcome()).isEqualTo(TaskOutcome.UP_TO_DATE);

        project.file(MASTER, changeLog(CREATE_TABLE_CHANGESET, INVALID_INDEX_CHANGESET));
        assertThat(project.buildAndFail(TASK).task(TASK).getOutcome()).isEqualTo(TaskOutcome.FAILED);
    }

    @Test
    void is_up_to_date_until_the_configuration_changes() {
        TestProject project = javaProject("").file(MASTER, changeLog(CREATE_TABLE_CHANGESET, INVALID_INDEX_CHANGESET));
        project.file("src/test/resources/lqlint.json", "{ \"rules\": {} }");

        assertThat(project.build(TASK).task(TASK).getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
        assertThat(project.build(TASK).task(TASK).getOutcome()).isEqualTo(TaskOutcome.UP_TO_DATE);

        project.file("src/test/resources/lqlint.json", LINT_CONFIGURATION);
        assertThat(project.buildAndFail(TASK).task(TASK).getOutcome()).isEqualTo(TaskOutcome.FAILED);
    }

    @Test
    void is_loaded_from_the_build_cache() {
        TestProject project = javaProject("").file(MASTER, changeLog(CREATE_TABLE_CHANGESET, VALID_INDEX_CHANGESET));
        project.file(
            "settings.gradle.kts",
            readSettings(project) +
                """

                buildCache {
                    local {
                        directory = file("build-cache")
                    }
                }
                """
        );

        assertThat(project.build(TASK, "--build-cache").task(TASK).getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
        project.build("clean");
        assertThat(project.build(TASK, "--build-cache").task(TASK).getOutcome()).isEqualTo(TaskOutcome.FROM_CACHE);
        assertThat(project.path("build/reports/liquibase-linter/liquibaseLinterCheck.txt")).exists();
    }

    @Test
    void is_compatible_with_the_configuration_cache() {
        TestProject project = javaProject("").file(MASTER, changeLog(CREATE_TABLE_CHANGESET, VALID_INDEX_CHANGESET));

        BuildResult first = project.build(
            "check",
            "--configuration-cache",
            "-Dorg.gradle.configuration-cache.problems=fail"
        );
        assertThat(first.getOutput()).contains("Configuration cache entry stored");

        BuildResult second = project.build(
            "check",
            "--configuration-cache",
            "-Dorg.gradle.configuration-cache.problems=fail"
        );
        assertThat(second.getOutput()).contains("Configuration cache entry reused");
        assertThat(second.task(TASK).getOutcome()).isEqualTo(TaskOutcome.UP_TO_DATE);
    }

    @Test
    void is_compatible_with_isolated_projects() {
        TestProject project = javaProject("").file(MASTER, changeLog(CREATE_TABLE_CHANGESET, VALID_INDEX_CHANGESET));

        BuildResult result = project.build("check", "-Dorg.gradle.unsafe.isolated-projects=true");

        assertThat(result.getOutput()).contains("Isolated Projects is an incubating feature");
        assertThat(result.task(TASK).getOutcome()).isEqualTo(TaskOutcome.SUCCESS);
    }

    @Test
    void reports_a_clear_error_when_the_configuration_file_is_missing() {
        TestProject project = javaProject("configurationFile = file(\"missing/lqlint.json\")").file(
            MASTER,
            changeLog(CREATE_TABLE_CHANGESET)
        );

        BuildResult result = project.buildAndFail("check");

        assertThat(result.getOutput()).contains("configurationFile", "missing/lqlint.json");
    }

    private static String readSettings(TestProject project) {
        try {
            return java.nio.file.Files.readString(project.path("settings.gradle.kts"));
        } catch (java.io.IOException e) {
            throw new java.io.UncheckedIOException(e);
        }
    }

    static void assertLintIssueIsReported(BuildResult result, String faultyChangeLog) {
        assertThat(result.getOutput()).containsSubsequence(
            "1 violation(s) found",
            faultyChangeLog,
            "\tchangeSet '2'",
            "\t\t" + INDEX_NAME_VIOLATION
        );
    }
}
