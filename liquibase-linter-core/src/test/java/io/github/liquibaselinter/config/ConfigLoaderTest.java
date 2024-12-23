package io.github.liquibaselinter.config;

import static io.github.liquibaselinter.config.ConfigLoader.LQLINT_CONFIG;
import static io.github.liquibaselinter.config.ConfigLoader.LQLINT_CONFIG_CLASSPATH;
import static io.github.liquibaselinter.config.ConfigLoader.LQLINT_CONFIG_PATH_PROPERTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.liquibaselinter.report.Reporter;
import java.io.IOException;
import java.io.InputStream;
import liquibase.exception.UnexpectedLiquibaseException;
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
        when(resourceAccessor.openStream(null, customPath)).thenReturn(getInputStream());
        when(resourceAccessor.openStream(null, LQLINT_CONFIG)).thenReturn(getInputStream());
        Config config = ConfigLoader.load(resourceAccessor);
        assertThat(config).isNotNull();
        verify(resourceAccessor, times(0)).openStreams(null, LQLINT_CONFIG);
    }

    @DisplayName("Should throw if cannot load config")
    @Test
    void shouldThrowIfCannotLoadConfig() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        when(resourceAccessor.openStreams(null, LQLINT_CONFIG)).thenReturn(null);

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> ConfigLoader.load(resourceAccessor))
            .withMessageContaining("Failed to load lq lint config file");
    }

    @DisplayName("Should throw on io exception")
    @Test
    void shouldThrowOnIoException() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        when(resourceAccessor.openStreams(null, LQLINT_CONFIG)).thenThrow(new IOException());

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> ConfigLoader.load(resourceAccessor))
            .withMessageContaining("Failed to load lq lint config file");
    }

    @DisplayName("Should import config")
    @Test
    void shouldImportConfig() throws IOException {
        ResourceAccessor resourceAccessor = mockResourceAccessor();
        when(resourceAccessor.openStream(null, "lqlint-import-a.test.json")).thenReturn(
            getInputStream("lqlint-import-a.test.json")
        );
        when(resourceAccessor.openStream(null, "lqlint-import-b.test.json")).thenReturn(
            getInputStream("lqlint-import-b.test.json")
        );

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
        when(resourceAccessor.openStream(null, "lqlint-import-a.test.json")).thenReturn(
            getInputStream("lqlint-import-a.test.json")
        );
        when(resourceAccessor.openStream(null, "lqlint-import-b.test.json")).thenReturn(
            getInputStream("lqlint-import-b.test.json")
        );

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
            .withMessageContaining("Failed to load imported lq lint config file");
    }

    @DisplayName("Should throw io exception when imported config fails with io exception")
    @Test
    void shouldThrowOnImportedIoException() throws IOException {
        ResourceAccessor resourceAccessor = mockResourceAccessor();
        when(resourceAccessor.openStream(null, "lqlint-import-a.test.json")).thenThrow(new IOException());

        assertThatExceptionOfType(UnexpectedLiquibaseException.class)
            .isThrownBy(() -> ConfigLoader.load(resourceAccessor))
            .withMessageContaining("Failed to load imported lq lint config file");
    }

    private ResourceAccessor mockResourceAccessor() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        when(resourceAccessor.openStream(null, LQLINT_CONFIG_CLASSPATH)).thenReturn(
            getInputStream("lqlint-importing.test.json")
        );
        return resourceAccessor;
    }

    private InputStream getInputStream() {
        return getInputStream("lqlint.test.json");
    }

    private InputStream getInputStream(String path) {
        return getClass().getClassLoader().getResourceAsStream(path);
    }
}
