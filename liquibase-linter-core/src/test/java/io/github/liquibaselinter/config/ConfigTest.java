package io.github.liquibaselinter.config;

import static io.github.liquibaselinter.report.ReportItem.ReportItemType.ERROR;
import static io.github.liquibaselinter.report.ReportItem.ReportItemType.IGNORED;
import static io.github.liquibaselinter.report.ReportItem.ReportItemType.PASSED;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
