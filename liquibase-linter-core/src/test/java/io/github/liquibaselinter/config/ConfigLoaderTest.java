package io.github.liquibaselinter.config;

import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.ResourceAccessor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static io.github.liquibaselinter.config.ConfigLoader.LQLINT_CONFIG;
import static io.github.liquibaselinter.config.ConfigLoader.LQLINT_CONFIG_CLASSPATH;
import static io.github.liquibaselinter.config.ConfigLoader.LQLINT_CONFIG_PATH_PROPERTY;
import static io.github.liquibaselinter.config.ConfigLoader.LQLINT_CONFIG_IMPLICIT_PATH;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConfigLoaderTest {

    private ConfigLoader configLoader;

    @AfterAll
    public static void tearDown() {
        System.clearProperty(LQLINT_CONFIG_PATH_PROPERTY);
    }

    @BeforeEach
    void setUp() {
        configLoader = new ConfigLoader();
    }

    @DisplayName("Should load from system property is present")
    @Test
    void shouldLoadFromSystemPropertyFirst() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        String customPath = "/test-lqlint.json";
        System.setProperty(LQLINT_CONFIG_PATH_PROPERTY, customPath);
        when(resourceAccessor.openStream(null, customPath)).thenReturn(getInputStream());
        when(resourceAccessor.openStream(null, LQLINT_CONFIG)).thenReturn(getInputStream());
        when(resourceAccessor.openStream(null, LQLINT_CONFIG_IMPLICIT_PATH))
            .thenReturn(getInputStream(LQLINT_CONFIG_IMPLICIT_PATH));
        Config config = configLoader.load(resourceAccessor);
        assertThat(config).isNotNull();
        verify(resourceAccessor, times(0)).openStreams(null, LQLINT_CONFIG);
    }

    @DisplayName("Should throw if cannot load config")
    @Test
    void shouldThrowIfCannotLoadConfig() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        when(resourceAccessor.openStreams(null, LQLINT_CONFIG)).thenReturn(null);

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> configLoader.load(resourceAccessor))
            .withMessageContaining("Failed to load lq lint config file");
    }

    @DisplayName("Should throw on io exception")
    @Test
    void shouldThrowOnIoException() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        when(resourceAccessor.openStreams(null, LQLINT_CONFIG)).thenThrow(new IOException());

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> configLoader.load(resourceAccessor))
            .withMessageContaining("Failed to load lq lint config file");
    }

    @DisplayName("Should import config")
    @Test
    void shouldImportConfig() throws IOException {
        ResourceAccessor resourceAccessor = mockResourceAccessor();
        when(resourceAccessor.openStream(null, "lqlint-import-a.test.json"))
            .thenReturn(getInputStream("lqlint-import-a.test.json"));
        when(resourceAccessor.openStream(null, "lqlint-import-b.test.json"))
            .thenReturn(getInputStream("lqlint-import-b.test.json"));

        Config config = configLoader.load(resourceAccessor);
        assertThat(config.getRules().asMap()).containsOnlyKeys("no-duplicate-includes", "no-preconditions", "file-name-no-spaces");
        assertThat(config.getRules().get("no-duplicate-includes")).extracting("enabled").containsExactly(true);
        assertThat(config.getRules().get("no-preconditions")).extracting("enabled").containsExactly(false);
        assertThat(config.getRules().get("file-name-no-spaces")).extracting("enabled").containsExactly(true);

        assertThat(config.getReporting().asMap()).containsOnlyKeys("console", "markdown");
        assertThat(config.getReporting().get("console")).extracting("configuration.enabled").containsExactly(false);
        assertThat(config.getReporting().get("markdown")).extracting("configuration.enabled").containsExactly(true);
    }

    @DisplayName("Should override imported config")
    @Test
    void shouldOverrideImportedConfig() throws IOException {
        ResourceAccessor resourceAccessor = mockResourceAccessor();
        when(resourceAccessor.openStream(null, "lqlint-import-a.test.json"))
            .thenReturn(getInputStream("lqlint-import-a.test.json"));
        when(resourceAccessor.openStream(null, "lqlint-import-b.test.json"))
            .thenReturn(getInputStream("lqlint-import-b.test.json"));

        Config config = configLoader.load(resourceAccessor);
        assertThat(config.getReporting().asMap()).containsOnlyKeys("console", "markdown");
        assertThat(config.getReporting().get("console")).extracting("configuration.enabled").containsExactly(false);
    }

    @DisplayName("Should throw io exception when imported config not found")
    @Test
    void shouldThrowOnMissingImportedConfig() throws IOException {
        ResourceAccessor resourceAccessor = mockResourceAccessor();

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> configLoader.load(resourceAccessor))
            .withMessageContaining("Failed to load imported lq lint config file");
    }

    @DisplayName("Should throw io exception when imported config fails with io exception")
    @Test
    void shouldThrowOnImportedIoException() throws IOException {
        ResourceAccessor resourceAccessor = mockResourceAccessor();
        when(resourceAccessor.openStream(null, "lqlint-import-a.test.json")).thenThrow(new IOException());

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> configLoader.load(resourceAccessor))
            .withMessageContaining("Failed to load imported lq lint config file");
    }

    private ResourceAccessor mockResourceAccessor() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        when(resourceAccessor.openStream(null, LQLINT_CONFIG_CLASSPATH))
            .thenReturn(getInputStream("lqlint-importing.test.json"));
        when(resourceAccessor.openStream(null, LQLINT_CONFIG_IMPLICIT_PATH))
            .thenReturn(getInputStream(LQLINT_CONFIG_IMPLICIT_PATH));
        return resourceAccessor;
    }

    private InputStream getInputStream() {
        return getInputStream("lqlint.test.json");
    }

    private InputStream getInputStream(String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }

}
