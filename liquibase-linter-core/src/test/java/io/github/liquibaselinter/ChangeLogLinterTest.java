package io.github.liquibaselinter;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableListMultimap;
import io.github.liquibaselinter.config.Config;
import io.github.liquibaselinter.config.RuleConfig;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ChangeLogLinterTest {

    @Test
    void shouldLoadConfigFromResourceAccessor(@TempDir Path directory) throws IOException {
        Files.writeString(
            directory.resolve("lqlint.json"),
            // language=json
            """
            { "rules": { "has-comment": true } }
            """
        );
        ChangeLogLinter linter = new ChangeLogLinter(new DirectoryResourceAccessor(directory));

        assertThatCode(() ->
            linter.lintChangeLog(new DatabaseChangeLog("changelog-without-extension"))
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldRequireValuesForFileNotIncludedRule() {
        ChangeLogLinter linter = linterWithFileNotIncludedRule(mock(ResourceAccessor.class), RuleConfig.enabled());
        DatabaseChangeLog databaseChangeLog = new DatabaseChangeLog("changelog.xml");

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> linter.lintChangeLog(databaseChangeLog))
            .withMessage("values not configured for rule `file-not-included`");
    }

    @Test
    void shouldIgnoreFileNotIncludedPathsThatCannotBeListed() throws IOException {
        ResourceAccessor resourceAccessor = mock(ResourceAccessor.class);
        when(resourceAccessor.search("unreadable/", true)).thenThrow(new IOException("Cannot list"));
        ChangeLogLinter linter = linterWithFileNotIncludedRule(
            resourceAccessor,
            RuleConfig.builder().withValues("unreadable/").build()
        );

        assertThatCode(() -> linter.lintChangeLog(new DatabaseChangeLog("changelog.xml"))).doesNotThrowAnyException();
    }

    private static ChangeLogLinter linterWithFileNotIncludedRule(
        ResourceAccessor resourceAccessor,
        RuleConfig ruleConfig
    ) {
        Config config = new Config.Builder()
            .withRules(ImmutableListMultimap.of("file-not-included", ruleConfig))
            .build();
        return new ChangeLogLinter(resourceAccessor, config);
    }
}
