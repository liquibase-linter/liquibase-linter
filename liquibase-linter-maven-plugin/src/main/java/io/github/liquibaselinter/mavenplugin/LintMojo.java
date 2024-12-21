package io.github.liquibaselinter.mavenplugin;

import com.google.common.collect.ImmutableListMultimap;
import io.github.liquibaselinter.ChangeLogLinter;
import io.github.liquibaselinter.ChangeLogLintingException;
import io.github.liquibaselinter.config.Config;
import io.github.liquibaselinter.config.ConfigLoader;
import io.github.liquibaselinter.report.ConsoleReporter;
import io.github.liquibaselinter.report.Reporter;
import io.github.liquibaselinter.report.ReporterConfig;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.DatabaseConnection;
import liquibase.database.OfflineConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.CompositeResourceAccessor;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

@Mojo(
    name = "lint",
    defaultPhase = LifecyclePhase.TEST,
    requiresDependencyResolution = ResolutionScope.COMPILE,
    threadSafe = true
)
public class LintMojo extends AbstractMojo {

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
                Liquibase liquibase = createLiquibase(relativePathOf(changeLogFile), resourceAccessor);
                DatabaseChangeLog databaseChangeLog = liquibase.getDatabaseChangeLog();

                Config linterConfig = linterConfiguration(resourceAccessor, configurationFile);
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

    private Config linterConfiguration(ResourceAccessor resourceAccessor, String configurationFile1)
        throws MojoExecutionException {
        Config linterConfig;
        try {
            Config userConfig = ConfigLoader.loadConfig(resourceAccessor, relativePathOf(configurationFile1));
            if (userConfig == null) {
                throw new MojoExecutionException("Unable to load lq-linter configuration at " + configurationFile1);
            }
            linterConfig = userConfig.mergeWith(defaultMavenLinterConfig());
        } catch (IOException exception) {
            throw new MojoExecutionException("ConfigurationFile " + configurationFile1 + " cannot be read", exception);
        }
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
        try {
            return new CompositeResourceAccessor(
                new DirectoryResourceAccessor(mavenProject.getBasedir()),
                new ClassLoaderResourceAccessor(Thread.currentThread().getContextClassLoader())
            );
        } catch (FileNotFoundException exception) {
            throw new MojoExecutionException(exception);
        }
    }

    private static Liquibase createLiquibase(String changeLogFile, ResourceAccessor resourceAccessor)
        throws LiquibaseException {
        try (DatabaseConnection connection = new OfflineConnection("offline:h2", resourceAccessor)) {
            return new Liquibase(changeLogFile, resourceAccessor, connection);
        }
    }
}
