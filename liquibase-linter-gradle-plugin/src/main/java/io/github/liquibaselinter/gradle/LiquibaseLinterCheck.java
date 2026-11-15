package io.github.liquibaselinter.gradle;

import io.github.liquibaselinter.gradle.internal.LintChangeLogAction;
import javax.inject.Inject;
import org.gradle.api.DefaultTask;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.api.tasks.CacheableTask;
import org.gradle.api.tasks.Classpath;
import org.gradle.api.tasks.IgnoreEmptyDirectories;
import org.gradle.api.tasks.Input;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.PathSensitive;
import org.gradle.api.tasks.PathSensitivity;
import org.gradle.api.tasks.TaskAction;
import org.gradle.workers.WorkerExecutor;

/**
 * Checks a Liquibase changelog, and all the changelogs it includes, against the configured lint rules.
 */
@CacheableTask
public abstract class LiquibaseLinterCheck extends DefaultTask {

    /**
     * The root changelog to lint: either a path relative to the project directory, or a resource of
     * {@link #getClasspath()}, optionally prefixed with {@code classpath:}.
     *
     * @return the root changelog
     */
    @Input
    public abstract Property<String> getChangeLogFile();

    /**
     * The liquibase-linter configuration file ({@code lqlint.json} or {@code lqlint.jsonc}).
     *
     * @return the configuration file
     */
    @InputFile
    @PathSensitive(PathSensitivity.NAME_ONLY)
    public abstract RegularFileProperty getConfigurationFile();

    /**
     * The files the changelogs are read from, when referenced relatively to the project directory.
     * Defaults to the resources of the main source set.
     *
     * @return the changelog sources
     */
    @InputFiles
    @IgnoreEmptyDirectories
    @PathSensitive(PathSensitivity.RELATIVE)
    public abstract ConfigurableFileCollection getChangeLogSources();

    /**
     * The classpath changelogs are looked up in, e.g. by {@code classpath:} includes.
     * Defaults to the runtime classpath of the main source set, without its compiled classes: the processed resources,
     * and the runtime dependencies.
     *
     * @return the classpath of the changelogs
     */
    @Classpath
    public abstract ConfigurableFileCollection getClasspath();

    /**
     * The classpath of the linter itself: liquibase-linter-core, Liquibase, and custom rules if any.
     *
     * @return the classpath of the linter
     */
    @Classpath
    public abstract ConfigurableFileCollection getLinterClasspath();

    /**
     * Whether the build should go on when violations are found.
     *
     * @return whether to ignore failures
     */
    @Input
    public abstract Property<Boolean> getIgnoreFailures();

    /**
     * The text report of the violations found.
     *
     * @return the report file
     */
    @OutputFile
    public abstract RegularFileProperty getReportFile();

    /**
     * The directory relative paths of changelogs and configuration are resolved against.
     *
     * @return the project directory
     */
    @Internal
    public abstract DirectoryProperty getProjectDirectory();

    /**
     * The service used to lint in an isolated classloader.
     *
     * @return the worker executor
     */
    @Inject
    protected abstract WorkerExecutor getWorkerExecutor();

    /**
     * Lints the changelog.
     */
    @TaskAction
    public void check() {
        getWorkerExecutor()
            .classLoaderIsolation(spec -> spec.getClasspath().from(getLinterClasspath()))
            .submit(LintChangeLogAction.class, parameters -> {
                parameters.getChangeLogFile().set(getChangeLogFile());
                parameters.getConfigurationFile().set(getConfigurationFile());
                parameters.getClasspath().from(getClasspath());
                parameters.getIgnoreFailures().set(getIgnoreFailures());
                parameters.getReportFile().set(getReportFile());
                parameters.getProjectDirectory().set(getProjectDirectory());
            });
    }
}
