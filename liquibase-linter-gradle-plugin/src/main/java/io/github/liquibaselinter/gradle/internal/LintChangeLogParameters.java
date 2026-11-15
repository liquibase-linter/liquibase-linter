package io.github.liquibaselinter.gradle.internal;

import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.file.DirectoryProperty;
import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;
import org.gradle.workers.WorkParameters;

/**
 * Parameters of {@link LintChangeLogAction}.
 */
public interface LintChangeLogParameters extends WorkParameters {
    /**
     * @return the root changelog
     */
    Property<String> getChangeLogFile();

    /**
     * @return the liquibase-linter configuration file
     */
    RegularFileProperty getConfigurationFile();

    /**
     * @return the classpath changelogs are looked up in
     */
    ConfigurableFileCollection getClasspath();

    /**
     * @return whether the build should go on when violations are found
     */
    Property<Boolean> getIgnoreFailures();

    /**
     * @return the text report of the violations
     */
    RegularFileProperty getReportFile();

    /**
     * @return the directory relative paths are resolved against
     */
    DirectoryProperty getProjectDirectory();
}
