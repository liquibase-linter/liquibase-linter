package io.github.liquibaselinter.gradle;

import static org.assertj.core.api.Assertions.assertThat;

import org.gradle.api.Project;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.language.base.plugins.LifecycleBasePlugin;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

class LiquibaseLinterPluginTest {

    private final Project project = ProjectBuilder.builder().build();

    @Test
    void registers_the_check_task_in_the_verification_group() {
        project.getPluginManager().apply("io.github.liquibase-linter");

        LiquibaseLinterCheck task = checkTask();
        assertThat(task.getGroup()).isEqualTo(LifecycleBasePlugin.VERIFICATION_GROUP);
        assertThat(task.getDescription()).isNotBlank();
        assertThat(project.getTasks().getByName(LifecycleBasePlugin.CHECK_TASK_NAME).getDependsOn()).anySatisfy(
            dependency -> assertThat(dependency.toString()).contains(LiquibaseLinterPlugin.TASK_NAME)
        );
    }

    @Test
    void uses_the_extension_as_convention_for_the_tasks() {
        project.getPluginManager().apply("io.github.liquibase-linter");
        LiquibaseLinterExtension extension = project.getExtensions().getByType(LiquibaseLinterExtension.class);

        extension.getChangeLogFile().set("db/master.xml");
        extension.getConfigurationFile().set(project.file("config/lqlint.json"));
        extension.getIgnoreFailures().set(true);

        LiquibaseLinterCheck task = checkTask();
        assertThat(task.getChangeLogFile().get()).isEqualTo("db/master.xml");
        assertThat(task.getConfigurationFile().get().getAsFile()).isEqualTo(project.file("config/lqlint.json"));
        assertThat(task.getIgnoreFailures().get()).isTrue();
        assertThat(task.getReportFile().get().getAsFile()).isEqualTo(
            project.file("build/reports/liquibase-linter/liquibaseLinterCheck.txt")
        );
    }

    @Test
    void has_sensible_defaults() {
        project.getPluginManager().apply("io.github.liquibase-linter");
        LiquibaseLinterExtension extension = project.getExtensions().getByType(LiquibaseLinterExtension.class);

        assertThat(extension.getConfigurationFile().get().getAsFile()).isEqualTo(
            project.file("src/test/resources/lqlint.json")
        );
        assertThat(extension.getIgnoreFailures().get()).isFalse();
        assertThat(extension.getSkip().get()).isFalse();
        assertThat(extension.getToolVersion().get()).isNotBlank();
        assertThat(extension.getLiquibaseVersion().get()).isNotBlank();
        assertThat(checkTask().getChangeLogSources().getFiles()).containsExactly(project.file("src/main/resources"));
    }

    @Test
    void declares_the_linter_and_liquibase_as_dependencies() {
        project.getPluginManager().apply("io.github.liquibase-linter");
        LiquibaseLinterExtension extension = project.getExtensions().getByType(LiquibaseLinterExtension.class);
        extension.getToolVersion().set("1.0.0");
        extension.getLiquibaseVersion().set("4.31.1");

        assertThat(project.getConfigurations().getByName(LiquibaseLinterPlugin.CONFIGURATION_NAME).getDependencies())
            .extracting(
                dependency -> dependency.getGroup() + ":" + dependency.getName() + ":" + dependency.getVersion()
            )
            .containsExactlyInAnyOrder(
                "io.github.liquibase-linter:liquibase-linter-core:1.0.0",
                "org.liquibase:liquibase-core:4.31.1"
            );
        assertThat(
            project.getConfigurations().getByName(LiquibaseLinterPlugin.CLASSPATH_CONFIGURATION_NAME).isCanBeResolved()
        ).isTrue();
    }

    @Test
    void uses_the_main_source_set_when_the_java_plugin_is_applied() {
        project.getPluginManager().apply("java");
        project.getPluginManager().apply("io.github.liquibase-linter");
        SourceSetContainer sourceSets = project.getExtensions().getByType(SourceSetContainer.class);

        LiquibaseLinterCheck task = checkTask();
        assertThat(task.getChangeLogSources().getFiles()).containsExactlyElementsOf(
            sourceSets.getByName("main").getResources().getSrcDirs()
        );
        assertThat(task.getClasspath().getFiles()).contains(sourceSets.getByName("main").getOutput().getResourcesDir());
    }

    @Test
    void configures_tasks_registered_by_the_user() {
        project.getPluginManager().apply("io.github.liquibase-linter");

        LiquibaseLinterCheck task = project.getTasks().register("lintLegacy", LiquibaseLinterCheck.class).get();

        assertThat(task.getReportFile().get().getAsFile()).isEqualTo(
            project.file("build/reports/liquibase-linter/lintLegacy.txt")
        );
        assertThat(task.getConfigurationFile().get().getAsFile()).isEqualTo(
            project.file("src/test/resources/lqlint.json")
        );
    }

    private LiquibaseLinterCheck checkTask() {
        return (LiquibaseLinterCheck) project.getTasks().getByName(LiquibaseLinterPlugin.TASK_NAME);
    }
}
