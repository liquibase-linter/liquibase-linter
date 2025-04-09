package io.github.liquibaselinter.mavenplugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import liquibase.resource.Resource;
import org.apache.maven.plugin.logging.Log;

public class SafeDirectoryResourceAccessor extends liquibase.resource.DirectoryResourceAccessor {

    private final Log log;

    public SafeDirectoryResourceAccessor(File directory, Log log) throws FileNotFoundException {
        super(directory);
        this.log = log;
    }

    @Override
    public List<Resource> getAll(String path) {
        try {
            return super.getAll(path);
        } catch (Exception e) {
            log.info(e.getMessage());
        }

        return new ArrayList<>();
    }
}
