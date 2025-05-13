package io.github.liquibaselinter.config;

import static java.lang.System.getProperty;
import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableListMultimap;
import io.github.liquibaselinter.report.ConsoleReporter;
import io.github.liquibaselinter.report.Reporter;
import io.github.liquibaselinter.report.ReporterConfig;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import liquibase.exception.UnexpectedLiquibaseException;
import liquibase.resource.Resource;
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
        for (String configPath : configPaths) {
            final Config config = loadConfig(resourceAccessor, configPath);
            if (config != null) {
                return config.combineWith(defaultConfig());
            }
        }
        throw new UnexpectedLiquibaseException(
            "Failed to load any liquibase-linter configuration from locations: " + String.join(", ", configPaths)
        );
    }

    private static Config defaultConfig() {
        ImmutableListMultimap.Builder<String, Reporter> reportingConfigBuilder = new ImmutableListMultimap.Builder<>();
        reportingConfigBuilder.put(
            ConsoleReporter.NAME,
            new ConsoleReporter(ReporterConfig.builder().withEnabled(true).build())
        );
        return new Config.Builder().withReporting(reportingConfigBuilder.build()).build();
    }

    public static Config loadConfig(ResourceAccessor resourceAccessor, String path) {
        try {
            Resource resource = resourceAccessor.get(path);
            if (resource == null || !resource.exists()) {
                return null;
            }
            Config config = Config.fromInputStream(resource.openInputStream());
            if (config != null) {
                return loadImports(resourceAccessor, config);
            }
        } catch (IOException exception) {
            throw new UnexpectedLiquibaseException("Failed to load liquibase-linter config file: " + path, exception);
        }
        return null;
    }

    private static Config loadImports(ResourceAccessor resourceAccessor, Config config) {
        List<String> imports = Collections.unmodifiableList(config.getImports());
        Config combinedImportConfig = new Config.Builder().build();
        for (String importPath : imports) {
            final Config importedConfig = loadConfig(resourceAccessor, importPath);
            if (importedConfig == null) {
                throw new UnexpectedLiquibaseException(
                    "Failed to load imported liquibase-linter config file: " + importPath
                );
            }
            combinedImportConfig = combinedImportConfig.mergeWith(importedConfig);
        }
        return config.combineWith(combinedImportConfig);
    }
}
