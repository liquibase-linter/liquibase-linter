package io.github.liquibaselinter.config;

import static io.github.liquibaselinter.config.ConfigLoader.LQLINT_CONFIG;
import static io.github.liquibaselinter.config.ConfigLoader.LQLINT_CONFIG_CLASSPATH;
import static io.github.liquibaselinter.config.ConfigLoader.LQLINT_CONFIG_PATH_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.liquibaselinter.report.Reporter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.PathResource;
import liquibase.resource.Resource;
import liquibase.resource.ResourceAccessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfigLoaderTest {

    @AfterAll
    static void tearDown() {
        System.clearProperty(LQLINT_CONFIG_PATH_PROPERTY);
    }

    @DisplayName("Should load from system property is present")
    @Test
    void shouldLoadFromSystemPropertyFirst() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        String customPath = "/test-lqlint.json";
        System.setProperty(LQLINT_CONFIG_PATH_PROPERTY, customPath);
        when(resourceAccessor.get(customPath)).thenReturn(pathResource("lqlint.test.json"));
        when(resourceAccessor.get(LQLINT_CONFIG)).thenReturn(pathResource("lqlint.test.json"));

        Config config = ConfigLoader.load(resourceAccessor);

        assertThat(config).isNotNull();
        verify(resourceAccessor, never()).get(LQLINT_CONFIG);
    }

    @DisplayName("Should throw if cannot load config")
    @Test
    void shouldThrowIfCannotLoadConfig() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        when(resourceAccessor.get(LQLINT_CONFIG)).thenReturn(null);

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> ConfigLoader.load(resourceAccessor))
            .withMessageContaining(
                "Failed to load any liquibase-linter configuration from locations: /lqlint.json, lqlint.json"
            );
    }

    @DisplayName("Should throw on io exception")
    @Test
    void shouldThrowOnIoException() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        when(resourceAccessor.get(LQLINT_CONFIG)).thenThrow(new IOException("file not found"));

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> ConfigLoader.load(resourceAccessor))
            .withMessageContaining("Failed to load liquibase-linter config file: /lqlint.json");
    }

    @DisplayName("Should import config")
    @Test
    void shouldImportConfig() throws IOException {
        ResourceAccessor resourceAccessor = mockResourceAccessor();
        when(resourceAccessor.get("lqlint-import-a.test.json")).thenReturn(pathResource("lqlint-import-a.test.json"));
        when(resourceAccessor.get("lqlint-import-b.test.json")).thenReturn(pathResource("lqlint-import-b.test.json"));

        Config config = ConfigLoader.load(resourceAccessor);

        assertThat(config.getRules().asMap()).containsOnlyKeys(
            "isolate-ddl-changes",
            "no-preconditions",
            "changelog-file-name"
        );
        assertThat(config.getRules().get("isolate-ddl-changes")).extracting("enabled").containsExactly(true);
        assertThat(config.getRules().get("no-preconditions")).extracting("enabled").containsExactly(false);
        assertThat(config.getRules().get("changelog-file-name")).extracting("enabled").containsExactly(true);

        assertThat(config.getReporting().asMap()).containsOnlyKeys("console", "markdown");
        assertThat(config.getReporting().get("console")).extracting(Reporter::isEnabled).containsExactly(false);
        assertThat(config.getReporting().get("markdown")).extracting(Reporter::isEnabled).containsExactly(true);
    }

    @DisplayName("Should override imported config")
    @Test
    void shouldOverrideImportedConfig() throws IOException {
        ResourceAccessor resourceAccessor = mockResourceAccessor();
        when(resourceAccessor.get("lqlint-import-a.test.json")).thenReturn(pathResource("lqlint-import-a.test.json"));
        when(resourceAccessor.get("lqlint-import-b.test.json")).thenReturn(pathResource("lqlint-import-b.test.json"));

        Config config = ConfigLoader.load(resourceAccessor);

        assertThat(config.getReporting().asMap()).containsOnlyKeys("console", "markdown");
        assertThat(config.getReporting().get("console")).extracting(Reporter::isEnabled).containsExactly(false);
    }

    @DisplayName("Should throw io exception when imported config not found")
    @Test
    void shouldThrowOnMissingImportedConfig() throws IOException {
        ResourceAccessor resourceAccessor = mockResourceAccessor();

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> ConfigLoader.load(resourceAccessor))
            .withMessageContaining("Failed to load imported liquibase-linter config file: lqlint-import-a.test.json");
    }

    @DisplayName("Should throw io exception when imported config fails with io exception")
    @Test
    void shouldThrowOnImportedIoException() throws IOException {
        ResourceAccessor resourceAccessor = mockResourceAccessor();
        when(resourceAccessor.get("lqlint-import-a.test.json")).thenThrow(new IOException());

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> ConfigLoader.load(resourceAccessor))
            .withMessageContaining("Failed to load liquibase-linter config file: lqlint-import-a.test.json");
    }

    private ResourceAccessor mockResourceAccessor() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        when(resourceAccessor.get(LQLINT_CONFIG_CLASSPATH)).thenReturn(pathResource("lqlint-importing.test.json"));
        return resourceAccessor;
    }

    private Resource pathResource(String path) {
        try {
            return new PathResource(path, Paths.get(getClass().getClassLoader().getResource(path).toURI()));
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }
}
