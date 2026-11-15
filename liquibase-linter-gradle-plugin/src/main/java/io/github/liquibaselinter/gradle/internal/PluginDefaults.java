package io.github.liquibaselinter.gradle.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

/**
 * Default versions of the tools, generated at build time of the plugin.
 *
 * @param toolVersion version of liquibase-linter-core
 * @param liquibaseVersion version of liquibase-core
 */
public record PluginDefaults(String toolVersion, String liquibaseVersion) {
    private static final String RESOURCE = "liquibase-linter-gradle-plugin.properties";

    /**
     * Loads the defaults embedded in the plugin.
     *
     * @return the defaults
     */
    public static PluginDefaults load() {
        try (
            InputStream input = PluginDefaults.class.getResourceAsStream(
                "/io/github/liquibaselinter/gradle/" + RESOURCE
            )
        ) {
            if (input == null) {
                throw new IllegalStateException("Missing " + RESOURCE + " in liquibase-linter Gradle plugin");
            }
            Properties properties = new Properties();
            properties.load(input);
            return new PluginDefaults(
                properties.getProperty("toolVersion"),
                properties.getProperty("liquibaseVersion")
            );
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + RESOURCE, e);
        }
    }
}
