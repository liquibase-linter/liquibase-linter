package io.github.liquibaselinter.gradle;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;

/**
 * A Gradle build applying the plugin, written in a temporary directory.
 */
final class TestProject {

    static final String INDEX_NAME_VIOLATION =
        "'index-name': Index names must be the table name, suffixed with 'IX' and a two-digit number, e.g. FOO_IX01";

    static final String LINT_CONFIGURATION = """
    {
      "fail-fast": false,
      "rules": {
        "index-name": {
          "pattern": "^{{value}}_IX\\\\d\\\\d$",
          "dynamicValue": "tableName",
          "errorMessage": "Index names must be the table name, suffixed with 'IX' and a two-digit number, e.g. FOO_IX01"
        }
      }
    }
    """;

    static final String CREATE_TABLE_CHANGESET = """
      <changeSet author="example" id="1">
        <createTable tableName="EXAMPLE_TABLE">
          <column name="FIRST" type="VARCHAR(128)" />
        </createTable>
      </changeSet>
    """;

    static final String INVALID_INDEX_CHANGESET = """
      <changeSet author="example" id="2">
        <createIndex indexName="EXAMPLE_INDEX" tableName="EXAMPLE_TABLE">
          <column name="FIRST" />
        </createIndex>
      </changeSet>
    """;

    static final String VALID_INDEX_CHANGESET = """
      <changeSet author="example" id="2">
        <createIndex indexName="EXAMPLE_TABLE_IX01" tableName="EXAMPLE_TABLE">
          <column name="FIRST" />
        </createIndex>
      </changeSet>
    """;

    private final Path directory;
    private String gradleVersion;

    TestProject(Path directory) {
        this.directory = directory;
        file(
            "settings.gradle.kts",
            """
            rootProject.name = "test-project"

            dependencyResolutionManagement {
                repositories {
                    %s {
                        content {
                            includeGroup("io.github.liquibase-linter")
                        }
                    }
                    mavenCentral()
                }
            }
            """.formatted(linterRepository())
        );
    }

    /**
     * The repository liquibase-linter-core is resolved from: the one staged by the Maven build, or Maven local.
     */
    private static String linterRepository() {
        String repository = System.getProperty("liquibaseLinter.repository");
        return repository == null ? "mavenLocal" : "maven(\"" + repository + "\")";
    }

    static String changeLog(String... changeSets) {
        return """
        <databaseChangeLog
          xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-latest.xsd"
        >
        %s
        </databaseChangeLog>
        """.formatted(String.join("\n", changeSets));
    }

    TestProject buildScript(String content) {
        return file("build.gradle.kts", content);
    }

    TestProject file(String path, String content) {
        try {
            Path file = directory.resolve(path);
            Files.createDirectories(file.getParent());
            Files.writeString(file, content);
            return this;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    Path path(String path) {
        return directory.resolve(path);
    }

    TestProject withGradleVersion(String gradleVersion) {
        this.gradleVersion = gradleVersion;
        return this;
    }

    BuildResult build(String... arguments) {
        return runner(arguments).build();
    }

    BuildResult buildAndFail(String... arguments) {
        return runner(arguments).buildAndFail();
    }

    private GradleRunner runner(String... arguments) {
        List<String> allArguments = new ArrayList<>(List.of(arguments));
        allArguments.add("--stacktrace");
        // The plugin must not use any deprecated Gradle feature
        allArguments.add("--warning-mode=fail");
        GradleRunner runner = GradleRunner.create()
            .withProjectDir(directory.toFile())
            .withPluginClasspath()
            .withArguments(allArguments)
            .forwardOutput();
        if (gradleVersion != null) {
            runner.withGradleVersion(gradleVersion);
        }
        return runner;
    }
}
