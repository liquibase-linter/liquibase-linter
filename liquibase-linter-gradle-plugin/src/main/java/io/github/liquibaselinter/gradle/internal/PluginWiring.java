package io.github.liquibaselinter.gradle.internal;

import io.github.liquibaselinter.gradle.LiquibaseLinterCheck;
import io.github.liquibaselinter.gradle.LiquibaseLinterExtension;
import io.github.liquibaselinter.gradle.LiquibaseLinterPlugin;
import org.gradle.api.NamedDomainObjectProvider;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.DependencyScopeConfiguration;
import org.gradle.api.artifacts.ResolvableConfiguration;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.attributes.AttributeContainer;
import org.gradle.api.attributes.Bundling;
import org.gradle.api.attributes.Category;
import org.gradle.api.attributes.LibraryElements;
import org.gradle.api.attributes.Usage;
import org.gradle.api.attributes.java.TargetJvmEnvironment;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.plugins.ReportingBasePlugin;
import org.gradle.api.provider.Provider;
import org.gradle.api.reporting.ReportingExtension;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskProvider;
import org.gradle.language.base.plugins.LifecycleBasePlugin;

/**
 * Wires the plugin into a project. Kept apart from {@link LiquibaseLinterPlugin}, so that the plugin class can be loaded
 * by Gradle versions lacking the APIs used here, and report that they are not supported.
 */
public final class PluginWiring {

    private PluginWiring() {}

    /**
     * Applies the plugin to the project.
     *
     * @param project the project
     */
    public static void apply(Project project) {
        project.getPluginManager().apply(LifecycleBasePlugin.class);
        project.getPluginManager().apply(ReportingBasePlugin.class);

        LiquibaseLinterExtension extension = createExtension(project);
        NamedDomainObjectProvider<ResolvableConfiguration> linterClasspath = createConfigurations(project, extension);
        configureTaskConventions(project, extension, linterClasspath);

        TaskProvider<LiquibaseLinterCheck> checkTask = project
            .getTasks()
            .register(LiquibaseLinterPlugin.TASK_NAME, LiquibaseLinterCheck.class, task -> {
                task.setGroup(LifecycleBasePlugin.VERIFICATION_GROUP);
                task.setDescription("Checks the Liquibase changelog against the liquibase-linter rules.");
            });
        project
            .getTasks()
            .named(LifecycleBasePlugin.CHECK_TASK_NAME)
            .configure(check -> check.dependsOn(checkTask));

        project.getPluginManager().withPlugin("java", javaPlugin -> configureJavaConventions(project));
    }

    private static LiquibaseLinterExtension createExtension(Project project) {
        PluginDefaults defaults = PluginDefaults.load();
        LiquibaseLinterExtension extension = project
            .getExtensions()
            .create(LiquibaseLinterExtension.NAME, LiquibaseLinterExtension.class);
        extension.getToolVersion().convention(defaults.toolVersion());
        extension.getLiquibaseVersion().convention(defaults.liquibaseVersion());
        extension
            .getConfigurationFile()
            .convention(project.getLayout().getProjectDirectory().file("src/test/resources/lqlint.json"));
        extension.getIgnoreFailures().convention(false);
        extension
            .getSkip()
            .convention(
                project
                    .getProviders()
                    .gradleProperty(LiquibaseLinterPlugin.SKIP_PROPERTY)
                    .map(Boolean::parseBoolean)
                    .orElse(false)
            );
        return extension;
    }

    private static NamedDomainObjectProvider<ResolvableConfiguration> createConfigurations(
        Project project,
        LiquibaseLinterExtension extension
    ) {
        DependencyHandler dependencies = project.getDependencies();
        NamedDomainObjectProvider<DependencyScopeConfiguration> linter = project
            .getConfigurations()
            .dependencyScope(LiquibaseLinterPlugin.CONFIGURATION_NAME, configuration -> {
                configuration.setDescription("The liquibase-linter libraries, and custom rules, used by this project.");
                configuration
                    .getDependencies()
                    .addLater(
                        extension
                            .getToolVersion()
                            .map(version ->
                                dependencies.create("io.github.liquibase-linter:liquibase-linter-core:" + version)
                            )
                    );
                configuration
                    .getDependencies()
                    .addLater(
                        extension
                            .getLiquibaseVersion()
                            .map(version -> dependencies.create("org.liquibase:liquibase-core:" + version))
                    );
            });
        return project
            .getConfigurations()
            .resolvable(LiquibaseLinterPlugin.CLASSPATH_CONFIGURATION_NAME, configuration -> {
                configuration.setDescription("The classpath of liquibase-linter.");
                configuration.extendsFrom(linter.get());
                configureJvmRuntimeAttributes(configuration, project.getObjects());
            });
    }

    private static void configureJvmRuntimeAttributes(Configuration configuration, ObjectFactory objects) {
        AttributeContainer attributes = configuration.getAttributes();
        attributes.attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage.class, Usage.JAVA_RUNTIME));
        attributes.attribute(Category.CATEGORY_ATTRIBUTE, objects.named(Category.class, Category.LIBRARY));
        attributes.attribute(
            LibraryElements.LIBRARY_ELEMENTS_ATTRIBUTE,
            objects.named(LibraryElements.class, LibraryElements.JAR)
        );
        attributes.attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.class, Bundling.EXTERNAL));
        attributes.attribute(
            TargetJvmEnvironment.TARGET_JVM_ENVIRONMENT_ATTRIBUTE,
            objects.named(TargetJvmEnvironment.class, TargetJvmEnvironment.STANDARD_JVM)
        );
    }

    private static void configureTaskConventions(
        Project project,
        LiquibaseLinterExtension extension,
        NamedDomainObjectProvider<ResolvableConfiguration> linterClasspath
    ) {
        ReportingExtension reporting = project.getExtensions().getByType(ReportingExtension.class);
        Provider<Boolean> skip = extension.getSkip();
        project
            .getTasks()
            .withType(LiquibaseLinterCheck.class)
            .configureEach(task -> {
                task.getChangeLogFile().convention(extension.getChangeLogFile());
                task.getConfigurationFile().convention(extension.getConfigurationFile());
                task.getIgnoreFailures().convention(extension.getIgnoreFailures());
                task.getLinterClasspath().convention(linterClasspath);
                task.getProjectDirectory().convention(project.getLayout().getProjectDirectory());
                task.getChangeLogSources().convention(
                    project.getLayout().getProjectDirectory().dir("src/main/resources")
                );
                task.getReportFile().convention(
                    reporting.getBaseDirectory().file("liquibase-linter/" + task.getName() + ".txt")
                );
                task.onlyIf("liquibase-linter is not skipped", unused -> !skip.get());
            });
    }

    private static void configureJavaConventions(Project project) {
        NamedDomainObjectProvider<SourceSet> main = project
            .getExtensions()
            .getByType(SourceSetContainer.class)
            .named(SourceSet.MAIN_SOURCE_SET_NAME);
        project
            .getTasks()
            .withType(LiquibaseLinterCheck.class)
            .configureEach(task -> {
                task.getClasspath().convention(main.map(SourceSet::getRuntimeClasspath));
                task.getChangeLogSources().convention(
                    main.map(sourceSet -> sourceSet.getResources().getSourceDirectories())
                );
            });
    }
}
