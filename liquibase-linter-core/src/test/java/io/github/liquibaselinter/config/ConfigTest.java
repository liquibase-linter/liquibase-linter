package io.github.liquibaselinter.config;

import static io.github.liquibaselinter.report.ReportItem.ReportItemType.ERROR;
import static io.github.liquibaselinter.report.ReportItem.ReportItemType.IGNORED;
import static io.github.liquibaselinter.report.ReportItem.ReportItemType.PASSED;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableListMultimap;
import io.github.liquibaselinter.report.Reporter;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ConfigTest {

    @DisplayName("Should support valid config object")
    @Test
    void shouldSupportValidConfigObject() throws IOException {
        String configJson =
            "{\n" +
            "  \"rules\": {\n" +
            "    \"schema-name\": {\n" +
            "      \"enabled\": true,\n" +
            "      \"pattern\": \"^\\\\$\\\\{[a-z_]+\\\\}$\",\n" +
            "      \"errorMessage\": \"Must use schema name token, not %s\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

        Config config = Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8));

        assertThat(config.getRules().size()).isEqualTo(1);
        assertThat(config.getRules().get("schema-name")).extracting("enabled").containsExactly(true);
    }

    @DisplayName("Should not support invalid config object")
    @Test
    void shouldNotSupportInValidConfigObject() throws IOException {
        String configJson = "{\n" + "  \"rules\": {\n" + "    \"isolate-ddl-changes\": \"foo\"\n" + "  }\n" + "}";

        assertThatExceptionOfType(JsonMappingException.class)
            .isThrownBy(() -> Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8)))
            .withMessageContaining("instance of `io.github.liquibaselinter.config.RuleConfig$RuleConfigBuilder`");
    }

    @DisplayName("Should support having rule config value as boolean")
    @Test
    void shouldSupportHavingRuleConfigAsBoolean() throws IOException {
        String configJson = "{\n" + "  \"rules\": {\n" + "    \"isolate-ddl-changes\": true\n" + "  }\n" + "}";

        Config config = Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8));

        assertThat(config.getRules().size()).isEqualTo(1);
        assertThat(config.getRules().get("isolate-ddl-changes")).extracting("enabled").containsExactly(true);
    }

    @DisplayName("Should support having an array of configs for one rule")
    @Test
    void shouldSupportArrayOfRuleConfigs() throws IOException {
        String configJson =
            "{\n" +
            "    \"rules\": {\n" +
            "        \"object-name\": [\n" +
            "            {\n" +
            "                \"pattern\": \"^(?!_)[A-Z_0-9]+(?<!_)$\",\n" +
            "                \"errorMessage\": \"Object name '%s' name must be uppercase and use '_' separation\"\n" +
            "            },\n" +
            "            {\n" +
            "                \"pattern\": \"^POWER.*$\",\n" +
            "                \"errorMessage\": \"Object name '%s' name must begin with 'POWER'\"\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}\n";

        Config config = Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8));

        assertThat(config.getRules().size()).isEqualTo(2);
    }

    @DisplayName("Should return disabled rule for null config object")
    @Test
    void shouldReturnDisabledRuleForNullConfigObject() throws IOException {
        String configJson = "{\n" + "  \"rules\": {\n" + "    \"isolate-ddl-changes\": null\n" + "  }\n" + "}";

        Config config = Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8));

        assertThat(config.getRules().size()).isEqualTo(1);
        assertThat(config.getRules().get("isolate-ddl-changes")).extracting("enabled").containsExactly(false);
    }

    @DisplayName("Should support a simple import")
    @Test
    void shouldSupportSimpleImport() throws IOException {
        String configJson = "{\n" + "  \"import\": \"imported.json\"\n" + "}";

        Config config = Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8));

        assertThat(config.getImports()).containsExactly("imported.json");
    }

    @DisplayName("Should support multiple imports")
    @Test
    void shouldSupportMultipleImports() throws IOException {
        String configJson =
            "{\n" + "  \"import\": [\n" + "    \"first.json\",\n" + "    \"second.json\"\n" + "  ]\n" + "}";

        Config config = Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8));

        assertThat(config.getImports()).containsExactly("first.json", "second.json");
    }

    @DisplayName("Should create read-only config with builder")
    @Test
    void shouldCreateReadOnlyConfigWithBuilder() {
        Config config = new Config.Builder()
            .withIgnoreContextPattern("abc")
            .withIgnoreFilesPattern("def")
            .withRules(ImmutableListMultimap.of("rule-name", RuleConfig.enabled()))
            .withFailFast(true)
            .withEnableAfter("after")
            .withImports("a", "b")
            .build();

        assertThat(config.getIgnoreContextPattern()).asString().isEqualTo("abc");
        assertThat(config.getIgnoreFilesPattern()).asString().isEqualTo("def");
        assertThat(config.getRules().asMap()).containsOnlyKeys("rule-name");
        assertThat(config.isFailFast()).isTrue();
        assertThat(config.getEnableAfter()).isEqualTo("after");
        assertThat(config.getImports()).containsExactly("a", "b");

        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() ->
            config.getRules().put("new-rule", RuleConfig.enabled())
        );
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() ->
            config.getImports().add("new-import")
        );
    }

    @DisplayName("Should copy existing config with builder")
    @Test
    void shouldCopyConfigWithBuilder() {
        Config config = new Config.Builder()
            .withIgnoreContextPattern("abc")
            .withIgnoreFilesPattern("def")
            .withRules(ImmutableListMultimap.of("rule-name", RuleConfig.enabled()))
            .withFailFast(true)
            .withEnableAfter("after")
            .withImports("a", "b")
            .build();

        Config copy = new Config.Builder(config).build();

        assertThat(config).usingRecursiveComparison().isEqualTo(copy);
    }

    @DisplayName("Should parse enable-after-changelog")
    @Test
    void shouldParseEnableAfterChangelog() throws IOException {
        String configJson = "{\n" + "  \"enable-after-changelog\": \"db/changelog/init.xml\"\n" + "}";

        Config config = Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8));

        assertThat(config.getEnableAfterChangelog()).isEqualTo("db/changelog/init.xml");
        assertThat(config.getEnableAfterChangeset()).isNull();
    }

    @DisplayName("Should parse enable-after-changeset")
    @Test
    void shouldParseEnableAfterChangeset() throws IOException {
        String configJson =
            "{\n" +
            "  \"enable-after-changeset\": {\n" +
            "    \"change-log-file\": \"db/changelog/init.xml\",\n" +
            "    \"id\": \"create-user-table\",\n" +
            "    \"author\": \"dba\"\n" +
            "  }\n" +
            "}";

        Config config = Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8));

        assertThat(config.getEnableAfterChangeset()).satisfies(changeset -> {
            assertThat(changeset.getChangeLogFile()).isEqualTo("db/changelog/init.xml");
            assertThat(changeset.getId()).isEqualTo("create-user-table");
            assertThat(changeset.getAuthor()).isEqualTo("dba");
        });
        assertThat(config.getEnableAfterChangelog()).isNull();
    }

    @DisplayName("Should accept the camelCase alias for enable-after-changelog / enable-after-changeset")
    @Test
    void shouldAcceptCamelCaseAliasForNewEnableAfterOptions() throws IOException {
        String changelogJson = "{\n" + "  \"enableAfterChangelog\": \"db/changelog/init.xml\"\n" + "}";
        String changesetJson =
            "{\n" +
            "  \"enableAfterChangeset\": { \"changeLogFile\": \"init.xml\", \"id\": \"create-user-table\", \"author\": \"dba\" }\n" +
            "}";

        Config changelogConfig = Config.fromInputStream(IOUtils.toInputStream(changelogJson, UTF_8));
        Config changesetConfig = Config.fromInputStream(IOUtils.toInputStream(changesetJson, UTF_8));

        assertThat(changelogConfig.getEnableAfterChangelog()).isEqualTo("db/changelog/init.xml");
        assertThat(changesetConfig.getEnableAfterChangeset())
            .extracting(ChangeSetIdentifier::getId)
            .isEqualTo("create-user-table");
    }

    @DisplayName("Should accept both kebab-case and camelCase spellings for project options")
    @Test
    void shouldAcceptBothSpellingsForProjectOptions() throws IOException {
        String kebab = "{\n" + "  \"fail-fast\": true,\n" + "  \"ignore-files-pattern\": \"^legacy/.*$\"\n" + "}";
        String camel = "{\n" + "  \"failFast\": true,\n" + "  \"ignoreFilesPattern\": \"^legacy/.*$\"\n" + "}";

        Config fromKebab = Config.fromInputStream(IOUtils.toInputStream(kebab, UTF_8));
        Config fromCamel = Config.fromInputStream(IOUtils.toInputStream(camel, UTF_8));

        assertThat(fromKebab.isFailFast()).isTrue();
        assertThat(fromKebab.getIgnoreFilesPattern()).asString().isEqualTo("^legacy/.*$");
        assertThat(fromCamel.isFailFast()).isTrue();
        assertThat(fromCamel.getIgnoreFilesPattern()).asString().isEqualTo("^legacy/.*$");
    }

    @DisplayName("Should accept both kebab-case and camelCase spellings for rule options")
    @Test
    void shouldAcceptBothSpellingsForRuleOptions() throws IOException {
        String kebab =
            "{\n" + "  \"rules\": { \"object-name\": { \"error-message\": \"msg\", \"max-length\": 30 } }\n" + "}";
        String camel =
            "{\n" + "  \"rules\": { \"object-name\": { \"errorMessage\": \"msg\", \"maxLength\": 30 } }\n" + "}";

        RuleConfig fromKebab = Config.fromInputStream(IOUtils.toInputStream(kebab, UTF_8))
            .forRule("object-name")
            .get(0);
        RuleConfig fromCamel = Config.fromInputStream(IOUtils.toInputStream(camel, UTF_8))
            .forRule("object-name")
            .get(0);

        assertThat(fromKebab.getErrorMessage()).isEqualTo("msg");
        assertThat(fromKebab.getMaxLength()).isEqualTo(30);
        assertThat(fromCamel.getErrorMessage()).isEqualTo("msg");
        assertThat(fromCamel.getMaxLength()).isEqualTo(30);
    }

    @DisplayName("Should resolve the legacy enable-after through getEnableAfterChangelog")
    @Test
    @SuppressWarnings("deprecation")
    void shouldResolveLegacyEnableAfterThroughGetEnableAfterChangelog() {
        Config config = new Config.Builder().withEnableAfter("legacy.xml").build();

        assertThat(config.getEnableAfter()).isEqualTo("legacy.xml");
        assertThat(config.getEnableAfterChangelog()).isEqualTo("legacy.xml");
    }

    @DisplayName("Should reject more than one enable-after option (builder)")
    @Test
    @SuppressWarnings("deprecation")
    void shouldRejectMultipleEnableAfterOptions() {
        assertThatThrownBy(() ->
            new Config.Builder().withEnableAfter("legacy.xml").withEnableAfterChangelog("changelog.xml").build()
        )
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Only one of");
    }

    @DisplayName("Should reject more than one enable-after option (JSON)")
    @Test
    void shouldRejectMultipleEnableAfterOptionsFromJson() {
        String configJson =
            "{\n" +
            "  \"enable-after-changelog\": \"changelog.xml\",\n" +
            "  \"enable-after-changeset\": { \"changeLogFile\": \"init.xml\", \"id\": \"create-user-table\", \"author\": \"dba\" }\n" +
            "}";

        assertThatExceptionOfType(JsonMappingException.class)
            .isThrownBy(() -> Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8)))
            .withMessageContaining("Only one of");
    }

    @DisplayName("Should reject an incomplete enable-after-changeset")
    @Test
    void shouldRejectIncompleteEnableAfterChangeset() {
        String configJson = "{\n" + "  \"enable-after-changeset\": { \"id\": \"create-user-table\" }\n" + "}";

        assertThatExceptionOfType(JsonMappingException.class)
            .isThrownBy(() -> Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8)))
            .withMessageContaining("'change-log-file', 'id' and 'author'");
    }

    @DisplayName("Should load reporting configuration")
    @Test
    void shouldSupportReporting() throws IOException {
        String configJson =
            "{\n" +
            "  \"reporting\": {\n" +
            "    \"text\": \"path/to/report.txt\",\n" +
            "    \"console\": {\n" +
            "      \"filter\": \"ERROR\"" +
            "    },\n" +
            "    \"markdown\": [\n" +
            "      {\n" +
            "        \"path\": \"path/to/report.md\"," +
            "        \"filter\": [\n" +
            "          \"ERROR\",\n" +
            "          \"IGNORED\",\n" +
            "          \"PASSED\"\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"path\": \"path/to/report2.md\"," +
            "        \"enabled\": true\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

        Config config = Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8));

        assertThat(config.getReporting().asMap()).containsOnlyKeys("text", "console", "markdown");

        assertThat(config.getReporting().get("text")).extracting("path").containsExactly("path/to/report.txt");

        assertThat(config.getReporting().get("console")).extracting(Reporter::isEnabled).containsExactly(true);
        assertThat(config.getReporting().get("console").get(0))
            .extracting("filter", as(InstanceOfAssertFactories.ITERABLE))
            .containsExactly(ERROR);

        assertThat(config.getReporting().get("markdown"))
            .extracting("path")
            .containsExactly("path/to/report.md", "path/to/report2.md");
        assertThat(config.getReporting().get("markdown").get(0))
            .extracting("filter", as(InstanceOfAssertFactories.ITERABLE))
            .containsExactly(ERROR, IGNORED, PASSED);
        assertThat(config.getReporting().get("markdown").get(1).isEnabled()).isTrue();
    }

    @DisplayName("Should not load with missing reporters")
    @Test
    void shouldNotLoadWithMissingReporters() throws IOException {
        String configJson = "{\n" + "  \"reporting\": {\n" + "    \"other\": false\n" + "  }\n" + "}";

        assertThatExceptionOfType(JsonMappingException.class)
            .isThrownBy(() -> Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8)))
            .withMessageContaining("No lq lint reporter named 'other'");
    }

    @DisplayName("Should support having comments in configuration")
    @Test
    void shouldSupportComments() throws IOException {
        String configJson =
            "{\n" +
            "  // Some comment \n" +
            "  /* Some comment */" +
            "  \"rules\": {\n" +
            "    \"isolate-ddl-changes\": true\n" +
            "  }\n" +
            "}";

        Config config = Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8));

        assertThat(config.getRules().size()).isEqualTo(1);
    }

    @DisplayName("Should support having trailing commas in configuration")
    @Test
    void shouldSupportTrailingCommas() throws IOException {
        String configJson = "{\n" + "  \"rules\": {\n" + "    \"isolate-ddl-changes\": true,\n" + "  },\n" + "}";

        Config config = Config.fromInputStream(IOUtils.toInputStream(configJson, UTF_8));

        assertThat(config.getRules().size()).isEqualTo(1);
    }
}
