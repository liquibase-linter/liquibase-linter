package io.github.liquibaselinter.config;

import static java.lang.System.getProperty;
import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableListMultimap;
import io.github.liquibaselinter.report.ConsoleReporter;
import io.github.liquibaselinter.report.Reporter;
import io.github.liquibaselinter.report.ReporterConfig;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.ResourceAccessor;

public final class ConfigLoader {

    public static final String LQLINT_CONFIG = "/lqlint.json";
    public static final String LQLINT_CONFIG_CLASSPATH = "lqlint.json";
    public static final String LQLINT_CONFIG_PATH_PROPERTY = "lqlint.config.path";

    private ConfigLoader() {}

    public static Config load(ResourceAccessor resourceAccessor) {
        List<String> configPaths = Stream.of(
            getProperty(LQLINT_CONFIG_PATH_PROPERTY),
            LQLINT_CONFIG,
            LQLINT_CONFIG_CLASSPATH
        )
            .filter(Objects::nonNull)
            .collect(toList());
        try {
            for (String configPath : configPaths) {
                final Config config = loadConfig(resourceAccessor, configPath);
                if (config != null) {
                    return config.combineWith(defaultConfig());
                }
            }
        } catch (IOException e) {
            throw new UnexpectedLiquibaseException("Failed to load lq lint config file", e);
        }
        throw new UnexpectedLiquibaseException("Failed to load lq lint config file");
    }

    private static Config defaultConfig() {
        ImmutableListMultimap.Builder<String, Reporter> reportingConfigBuilder = new ImmutableListMultimap.Builder<>();
        reportingConfigBuilder.put("console", new ConsoleReporter(ReporterConfig.builder().withEnabled(true).build()));
        return new Config.Builder().withReporting(reportingConfigBuilder.build()).build();
    }

    public static Config loadConfig(ResourceAccessor resourceAccessor, String path) throws IOException {
        try (InputStream stream = resourceAccessor.openStream(null, path)) {
            if (stream != null) {
                final Config config = Config.fromInputStream(stream);
                if (config != null) {
                    return loadImports(resourceAccessor, config);
                }
            }
        }
        return null;
    }

    private static Config loadImports(ResourceAccessor resourceAccessor, Config config) {
        List<String> imports = Collections.unmodifiableList(config.getImports());
        Config combinedImportConfig = new Config.Builder().build();
        for (String importPath : imports) {
            try {
                final Config importedConfig = loadConfig(resourceAccessor, importPath);
                combinedImportConfig = combinedImportConfig.mergeWith(importedConfig);
            } catch (IOException | NullPointerException e) {
                throw new UnexpectedLiquibaseException("Failed to load imported lq lint config file: " + importPath, e);
            }
        }
        return config.combineWith(combinedImportConfig);
    }
}
