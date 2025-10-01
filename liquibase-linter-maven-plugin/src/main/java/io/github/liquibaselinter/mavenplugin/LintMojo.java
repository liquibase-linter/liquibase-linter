package io.github.liquibaselinter.mavenplugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.ResolutionScope;

@Mojo(
    name = "lint",
    defaultPhase = LifecyclePhase.TEST,
    requiresDependencyResolution = ResolutionScope.COMPILE,
    threadSafe = true
)
@Deprecated
public class LintMojo extends CheckMojo {

    @Override
    public void execute() throws MojoFailureException, MojoExecutionException {
        getLog()
            .warn(
                "The 'lint' goal is deprecated and will be removed in a future release. Please use the 'check' goal instead."
            );
        super.execute();
    }
}
