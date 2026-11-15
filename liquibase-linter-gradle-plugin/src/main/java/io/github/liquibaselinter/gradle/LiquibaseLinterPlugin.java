package io.github.liquibaselinter.gradle;

import io.github.liquibaselinter.gradle.internal.PluginWiring;
import org.gradle.api.GradleException;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.util.GradleVersion;

/**
 * Registers the {@value #TASK_NAME} task, run by {@code check}, which lints the Liquibase changelog configured in the
 * {@value LiquibaseLinterExtension#NAME} extension.
 */
public abstract class LiquibaseLinterPlugin implements Plugin<Project> {

    /**
     * Name of the task linting the changelog configured in the extension.
     */
    public static final String TASK_NAME = "liquibaseLinterCheck";

    /**
     * Name of the configuration holding the linter dependencies: custom rules can be added to it.
     */
    public static final String CONFIGURATION_NAME = "liquibaseLinter";

    /**
     * Name of the configuration resolving the linter classpath.
     */
    public static final String CLASSPATH_CONFIGURATION_NAME = "liquibaseLinterClasspath";

    /**
     * Name of the Gradle property allowing to skip the check, e.g. {@code -Pliquibase-linter.skip=true}.
     */
    public static final String SKIP_PROPERTY = "liquibase-linter.skip";

    /**
     * The oldest Gradle version supported by the plugin.
     */
    public static final String MINIMUM_GRADLE_VERSION = "8.8";

    @Override
    public void apply(Project project) {
        if (GradleVersion.current().compareTo(GradleVersion.version(MINIMUM_GRADLE_VERSION)) < 0) {
            throw new GradleException(
                "The liquibase-linter Gradle plugin requires Gradle " +
                    MINIMUM_GRADLE_VERSION +
                    " or later, but this build uses " +
                    GradleVersion.current().getVersion() +
                    "."
            );
        }
        PluginWiring.apply(project);
    }
}
