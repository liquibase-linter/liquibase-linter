package com.whiteclarkegroup.liquibaselinter.resolvers;

import com.whiteclarkegroup.liquibaselinter.config.ConfigLoader;
import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.OfflineConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.InputStreamList;
import liquibase.resource.ResourceAccessor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.Optional;

public class LiquibaseIntegrationTestResolver implements ParameterResolver {

    public static Liquibase buildLiquibase(String changeLogFile, String configFile) throws LiquibaseException {
        ResourceAccessor resourceAccessor = new ConfigAwareResourceAccessor("integration/" + configFile);
        DatabaseConnection conn = new OfflineConnection("offline:h2", resourceAccessor);
        return new Liquibase("integration/" + changeLogFile, resourceAccessor, conn);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == Liquibase.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        try {
            Optional<Method> method = extensionContext.getTestMethod();
            if (method.isPresent()) {
                LiquibaseLinterIntegrationTest testConfig = method.get().getAnnotation(LiquibaseLinterIntegrationTest.class);
                return buildLiquibase(testConfig.changeLogFile(), testConfig.configFile());
            } else {
                throw new ParameterResolutionException("Failed to create liquibase parameter");
            }
        } catch (LiquibaseException e) {
            throw new ParameterResolutionException("Failed to create liquibase parameter");
        }
    }

    private static class ConfigAwareResourceAccessor extends ClassLoaderResourceAccessor {

        private final String configPath;

        private ConfigAwareResourceAccessor(String configPath) {
            this.configPath = configPath;
        }

        @Override
        public InputStream openStream(String relativeTo, String streamPath) throws IOException {
            if (relativeTo == null && ConfigLoader.LQLINT_CONFIG_CLASSPATH.equals(streamPath)) {
                return getClass().getClassLoader().getResourceAsStream(configPath);
            }
            return super.openStream(relativeTo, streamPath);
        }

        @Override
        public InputStreamList openStreams(String relativeTo, String streamPath) throws IOException {
            if (relativeTo == null && ConfigLoader.LQLINT_CONFIG_CLASSPATH.equals(streamPath)) {
                return new InputStreamList(URI.create(streamPath), getClass().getResourceAsStream(configPath));
            }
            return super.openStreams(relativeTo, streamPath);
        }
    }

}


