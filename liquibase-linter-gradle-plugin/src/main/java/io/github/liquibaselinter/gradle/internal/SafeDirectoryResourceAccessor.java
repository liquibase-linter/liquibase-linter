package io.github.liquibaselinter.gradle.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.Resource;
import org.gradle.api.logging.Logger;

/**
 * A {@link DirectoryResourceAccessor} which does not fail on paths it cannot resolve, e.g. {@code classpath:}
 * ones, so that the other accessors of a composite one get a chance to resolve them.
 */
class SafeDirectoryResourceAccessor extends DirectoryResourceAccessor {

    private final Logger logger;

    SafeDirectoryResourceAccessor(File directory, Logger logger) throws FileNotFoundException {
        super(directory);
        this.logger = logger;
    }

    @Override
    public List<Resource> getAll(String path) {
        try {
            return super.getAll(path);
        } catch (Exception e) {
            logger.debug("Cannot resolve '{}' in the project directory: {}", path, e.getMessage());
            return List.of();
        }
    }
}
