package io.github.liquibaselinter.gradle;

import org.gradle.api.file.RegularFileProperty;
import org.gradle.api.provider.Property;

/**
 * Project-wide configuration of the Liquibase Linter plugin, exposed as {@code liquibaseLinter { ... }}.
 * <p>
 * Every value is used as a convention for all the {@link LiquibaseLinterCheck} tasks of the project, which can still
 * override it individually.
 */
public abstract class LiquibaseLinterExtension {

    /**
     * Name of the extension.
     */
    public static final String NAME = "liquibaseLinter";

    /**
     * Version of {@code io.github.liquibase-linter:liquibase-linter-core} used to lint the changelogs.
     * Defaults to the version of this plugin.
     *
     * @return the linter version
     */
    public abstract Property<String> getToolVersion();

    /**
     * Version of {@code org.liquibase:liquibase-core} used to parse the changelogs.
     *
     * @return the Liquibase version
     */
    public abstract Property<String> getLiquibaseVersion();

    /**
     * The root changelog to lint: either a path relative to the project directory
     * (e.g. {@code src/main/resources/db/changelog/master.xml}), or a resource of the main runtime classpath,
     * optionally prefixed with {@code classpath:}.
     *
     * @return the root changelog
     */
    public abstract Property<String> getChangeLogFile();

    /**
     * The liquibase-linter configuration file ({@code lqlint.json} or {@code lqlint.jsonc}).
     * Defaults to {@code src/test/resources/lqlint.json}.
     *
     * @return the configuration file
     */
    public abstract RegularFileProperty getConfigurationFile();

    /**
     * Whether the build should go on when violations are found. Defaults to {@code false}.
     *
     * @return whether to ignore failures
     */
    public abstract Property<Boolean> getIgnoreFailures();

    /**
     * Skips the linting of the changelog. Defaults to the value of the {@code liquibase-linter.skip} Gradle property,
     * e.g. {@code ./gradlew check -Pliquibase-linter.skip=true}, or {@code false}.
     *
     * @return whether to skip the linting
     */
    public abstract Property<Boolean> getSkip();
}
