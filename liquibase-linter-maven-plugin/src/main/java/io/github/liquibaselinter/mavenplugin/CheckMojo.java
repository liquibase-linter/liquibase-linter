package io.github.liquibaselinter.mavenplugin;

import com.google.common.collect.ImmutableListMultimap;
import io.github.liquibaselinter.ChangeLogLinter;
import io.github.liquibaselinter.ChangeLogLintingException;
import io.github.liquibaselinter.config.Config;
import io.github.liquibaselinter.config.ConfigLoader;
import io.github.liquibaselinter.report.ConsoleReporter;
import io.github.liquibaselinter.report.Reporter;
import io.github.liquibaselinter.report.ReporterConfig;
import java.io.File;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.inject.Inject;
import liquibase.Scope;
import liquibase.changelog.ChangeLogParameters;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.exception.LiquibaseException;
import liquibase.integration.spring.SpringResourceAccessor;
import liquibase.parser.ChangeLogParser;
import liquibase.parser.ChangeLogParserFactory;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.springframework.core.io.DefaultResourceLoader;

@Mojo(
    name = "check",
    defaultPhase = LifecyclePhase.TEST,
    requiresDependencyResolution = ResolutionScope.COMPILE,
    threadSafe = true
)
public class CheckMojo extends AbstractMojo {

    @Parameter(property = "changeLogFile", required = true)
    private String changeLogFile;

    @Parameter(property = "configurationFile", defaultValue = "src/test/resources/lqlint.json", required = true)
    private String configurationFile;

    /**
     * The active Maven project.
     */
    @Inject
    private MavenProject mavenProject;

    @Override
    public void execute() throws MojoFailureException, MojoExecutionException {
        try (ResourceAccessor resourceAccessor = buildResourceAccessor()) {
            Scope.child(setUpLiquibaseLogging(), () -> {
                DatabaseChangeLog databaseChangeLog = parseChangeLog(relativePathOf(changeLogFile), resourceAccessor);

                Config linterConfig = linterConfiguration(resourceAccessor);
                new ChangeLogLinter(resourceAccessor, linterConfig).lintChangeLog(databaseChangeLog);
            });
        } catch (ChangeLogLintingException lintingException) {
            throw new MojoFailureException(lintingException);
        } catch (Exception e) {
            throw new MojoExecutionException(e);
        }
    }

    private Map<String, Object> setUpLiquibaseLogging() {
        Map<String, Object> scopeAttrs = new HashMap<>();
        scopeAttrs.put(Scope.Attr.logService.name(), new LiquibaseMavenLogService(getLog()));
        return scopeAttrs;
    }

    private Config linterConfiguration(ResourceAccessor resourceAccessor) throws MojoExecutionException {
        Config linterConfig;
        Config userConfig = ConfigLoader.loadConfig(resourceAccessor, relativePathOf(configurationFile));
        if (userConfig == null) {
            throw new MojoExecutionException("Unable to load liquibase-linter configuration at " + configurationFile);
        }
        linterConfig = userConfig.mergeWith(defaultMavenLinterConfig());
        return linterConfig;
    }

    private Config defaultMavenLinterConfig() {
        ImmutableListMultimap.Builder<String, Reporter> reportingConfigBuilder = new ImmutableListMultimap.Builder<>();
        reportingConfigBuilder.put("mavenReporter", new MavenConsoleReporter(getLog()));
        reportingConfigBuilder.put("console", new ConsoleReporter(ReporterConfig.builder().withEnabled(false).build()));
        return new Config.Builder().withReporting(reportingConfigBuilder.build()).build();
    }

    private String relativePathOf(String changeLogFile) {
        return changeLogFile.replace(mavenProject.getBasedir().getAbsolutePath(), "");
    }

    private ResourceAccessor buildResourceAccessor() throws MojoExecutionException {
        try (
            SpringResourceAccessor springResourceAccessor = new SpringResourceAccessor(
                new DefaultResourceLoader(classLoaderIncludingProjectClasspath())
            );
            ResourceAccessor baseDirResourceAccessor = new SafeDirectoryResourceAccessor(
                mavenProject.getBasedir(),
                getLog()
            );
        ) {
            return new CompositeResourceAccessor(springResourceAccessor, baseDirResourceAccessor);
        } catch (Exception exception) {
            throw new MojoExecutionException(exception);
        }
    }

    private ClassLoader classLoaderIncludingProjectClasspath() throws MojoExecutionException {
        try {
            URL[] urls = mavenProject
                .getCompileClasspathElements()
                .stream()
                .map(File::new)
                .map(fileToURL())
                .toArray(URL[]::new);
            return new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        } catch (Exception exception) {
            throw new MojoExecutionException("Failed to create project classloader", exception);
        }
    }

    private static Function<File, URL> fileToURL() {
        return file -> {
            try {
                return file.toURI().toURL();
            } catch (MalformedURLException exception) {
                throw new UncheckedIOException(exception);
            }
        };
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
        Scope.getCurrentScope()
            .getLog(CheckMojo.class)
            .info("Parsed changelog file '" + changeLogFile + "'");
        return databaseChangeLog;
    }
}
