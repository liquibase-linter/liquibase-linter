package io.github.liquibaselinter.gradle.internal;

import java.io.IOException;
import java.util.List;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.Resource;

/**
 * A {@link ClassLoaderResourceAccessor} supporting the {@code classpath:} prefix, as Liquibase does when run by Spring.
 */
class ClasspathResourceAccessor extends ClassLoaderResourceAccessor {

    private static final String CLASSPATH_PREFIX = "classpath:";

    ClasspathResourceAccessor(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    public List<Resource> getAll(String path) throws IOException {
        return super.getAll(withoutPrefix(path));
    }

    @Override
    public List<Resource> search(String path, boolean recursive) throws IOException {
        return super.search(withoutPrefix(path), recursive);
    }

    private static String withoutPrefix(String path) {
        if (path != null && path.startsWith(CLASSPATH_PREFIX)) {
            return path.substring(CLASSPATH_PREFIX.length());
        }
        return path;
    }
}
