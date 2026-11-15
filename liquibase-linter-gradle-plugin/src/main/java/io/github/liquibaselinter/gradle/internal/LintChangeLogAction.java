package io.github.liquibaselinter.gradle.internal;

import com.google.common.collect.ImmutableListMultimap;
import io.github.liquibaselinter.ChangeLogLinter;
import io.github.liquibaselinter.ChangeLogLintingException;
import io.github.liquibaselinter.config.Config;
import io.github.liquibaselinter.config.ConfigLoader;
import io.github.liquibaselinter.report.Reporter;
import io.github.liquibaselinter.report.ReporterConfig;
import io.github.liquibaselinter.report.TextReporter;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import liquibase.Scope;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.gradle.api.GradleException;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import org.gradle.workers.WorkAction;

/**
 * Lints a changelog. Runs in a classloader isolated from the build, holding liquibase-linter and Liquibase.
 */
public abstract class LintChangeLogAction implements WorkAction<LintChangeLogParameters> {

    private static final Logger LOGGER = Logging.getLogger(LintChangeLogAction.class);

    @Override
    public void execute() {
        // liquibase-linter and Liquibase discover their rules, parsers... with the context classloader
        Thread thread = Thread.currentThread();
        ClassLoader buildClassLoader = thread.getContextClassLoader();
        thread.setContextClassLoader(LintChangeLogAction.class.getClassLoader());
        try {
            lint();
        } finally {
            thread.setContextClassLoader(buildClassLoader);
        }
    }

    private void lint() {
        LintChangeLogParameters parameters = getParameters();
        File projectDirectory = parameters.getProjectDirectory().get().getAsFile();
        String changeLogFile = relativePath(projectDirectory, parameters.getChangeLogFile().get());
        File reportFile = parameters.getReportFile().get().getAsFile();
        try (
            URLClassLoader classLoader = projectClassLoader(parameters);
            ResourceAccessor resourceAccessor = new CompositeResourceAccessor(
                new ClasspathResourceAccessor(classLoader),
                new SafeDirectoryResourceAccessor(projectDirectory, LOGGER)
            )
        ) {
            Scope.child(Map.of(Scope.Attr.logService.name(), new GradleLogService(LOGGER)), () -> {
                DatabaseChangeLog databaseChangeLog = parseChangeLog(changeLogFile, resourceAccessor);
                Config config = loadConfig(projectDirectory, resourceAccessor).mergeWith(gradleConfig(reportFile));
                new ChangeLogLinter(resourceAccessor, config).lintChangeLog(databaseChangeLog);
            });
        } catch (ChangeLogLintingException lintingException) {
            handleViolations(lintingException, reportFile);
        } catch (GradleException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new GradleException(
                "Failed to lint Liquibase changelog '" + changeLogFile + "': " + exception.getMessage(),
                exception
            );
        }
    }

    private void handleViolations(ChangeLogLintingException lintingException, File reportFile) {
        if (!reportFile.exists()) {
            // Some violations, e.g. of 'file-not-included', are raised before the reporters are called
            writeReport(reportFile, lintingException.getMessage());
        }
        String message = lintingException.getMessage() + "\n\nSee the report at: " + reportFile.toPath().toUri();
        if (getParameters().getIgnoreFailures().get()) {
            LOGGER.warn(message);
        } else {
            throw new GradleException(message);
        }
    }

    private static void writeReport(File reportFile, String content) {
        try {
            Files.writeString(reportFile.toPath(), content);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot write liquibase-linter report " + reportFile, e);
        }
    }

    private static URLClassLoader projectClassLoader(LintChangeLogParameters parameters) {
        URL[] urls = parameters
            .getClasspath()
            .getFiles()
            .stream()
            .map(LintChangeLogAction::toUrl)
            .toArray(URL[]::new);
        // No parent: changelogs must only be looked up in the project classpath, not in the linter's one
        return new URLClassLoader(urls, null);
    }

    private static URL toUrl(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new UncheckedIOException(e);
        }
    }

    private Config loadConfig(File projectDirectory, ResourceAccessor projectResourceAccessor) throws Exception {
        File configurationFile = getParameters().getConfigurationFile().get().getAsFile();
        Path configurationPath = configurationFile.toPath().toAbsolutePath().normalize();
        Path projectPath = projectDirectory.toPath().toAbsolutePath().normalize();
        Config config;
        if (configurationPath.startsWith(projectPath)) {
            // Resolved like changelogs, so that imported configurations can be found the same way
            config = ConfigLoader.loadConfig(
                projectResourceAccessor,
                toLiquibasePath(projectPath.relativize(configurationPath))
            );
        } else {
            try (
                ResourceAccessor configurationDirectory = new SafeDirectoryResourceAccessor(
                    configurationPath.getParent().toFile(),
                    LOGGER
                )
            ) {
                config = ConfigLoader.loadConfig(configurationDirectory, configurationPath.getFileName().toString());
            }
        }
        if (config == null) {
            throw new GradleException("Unable to load liquibase-linter configuration at " + configurationFile);
        }
        return config;
    }

    private static Config gradleConfig(File reportFile) {
        ImmutableListMultimap.Builder<String, Reporter> reporting = new ImmutableListMultimap.Builder<>();
        reporting.put("gradle", new GradleLoggerReporter(LOGGER));
        reporting.put(
            TextReporter.NAME,
            new TextReporter(ReporterConfig.builder().withPath(reportFile.getAbsolutePath()).build())
        );
        return new Config.Builder().withReporting(reporting.build()).build();
    }

    private static String relativePath(File projectDirectory, String path) {
        Path projectPath = projectDirectory.toPath().toAbsolutePath().normalize();
        Path candidate = Path.of(path);
        if (candidate.isAbsolute() && candidate.normalize().startsWith(projectPath)) {
            return toLiquibasePath(projectPath.relativize(candidate.normalize()));
        }
        return path;
    }

    private static String toLiquibasePath(Path path) {
        return path.toString().replace(File.separatorChar, '/');
    }

    /**
     * Parses the change log without a database. Liquibase drops every changeset whose {@code dbms} does not match
     * the database it parses for, so parsing for any one database would hide the changesets meant for the others
     * from the rules. Without a database, every changeset is kept.
     */
    private static DatabaseChangeLog parseChangeLog(String changeLogFile, ResourceAccessor resourceAccessor)
        throws LiquibaseException {
        ChangeLogParser parser = ChangeLogParserFactory.getInstance().getParser(changeLogFile, resourceAccessor);
        DatabaseChangeLog databaseChangeLog = parser.parse(changeLogFile, new ChangeLogParameters(), resourceAccessor);
        LOGGER.info("Parsed changelog file '{}'", changeLogFile);
        return databaseChangeLog;
    }
}
