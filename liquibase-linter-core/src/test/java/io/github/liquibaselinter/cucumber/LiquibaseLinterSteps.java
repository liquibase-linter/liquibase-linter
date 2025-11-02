package io.github.liquibaselinter.cucumber;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.liquibaselinter.ChangeLogLinter;
import io.github.liquibaselinter.ChangeLogLintingException;
import io.github.liquibaselinter.config.Config;
import io.github.liquibaselinter.config.ConfigLoader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import liquibase.Liquibase;
import liquibase.changelog.DatabaseChangeLog;
import liquibase.database.DatabaseConnection;
import liquibase.database.OfflineConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.DirectoryResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LiquibaseLinterSteps {

    private static final Log log = LogFactory.getLog(LiquibaseLinterSteps.class);

    private Path tempDirectory;
    private String databaseChangeLog;
    private String configFile;
    private Optional<String> actualErrorMessage;

    @Before
    public void setup() throws IOException {
        tempDirectory = Files.createTempDirectory("liquibase-linter-test-");
        log.info("Created temporary directory for test: " + tempDirectory.toAbsolutePath());
        databaseChangeLog = null;
        configFile = null;
        actualErrorMessage = Optional.empty();
    }

    @Given("liquibase-linter is configured with")
    public void liquibaseLinterIsConfiguredWith(String configurationContent) throws IOException {
        configFile = "lqlint.json";
        Path configurationPath = tempDirectory.resolve(configFile);
        Files.write(configurationPath, configurationContent.getBytes());
    }

    @Given("rule {string} is enabled with the configuration")
    public void ruleIsEnabledWithTheFollowingConfiguration(String ruleName, Map<String, String> ruleConfiguration)
        throws IOException {
        String configurationContent =
            "{\n" +
            "  \"fail-fast\": true," +
            "  \"rules\": {\n" +
            "    \"" +
            ruleName +
            "\": [\n" +
            "      {\n" +
            "        \"enabled\": true,\n" +
            ruleConfiguration
                .entrySet()
                .stream()
                .map(entry -> String.format("\"%s\": \"%s\"", entry.getKey(), entry.getValue()))
                .collect(Collectors.joining(", ")) +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";
        liquibaseLinterIsConfiguredWith(configurationContent);
    }

    @Given("the main XML changelog file contains changes")
    public void mainChangelogFileContainsChanges(String changesContent) throws IOException {
        String changeSetsContent = "<changeSet id=\"1\" author=\"tester\">\n" + changesContent + "\n</changeSet>";
        mainChangelogFileContainsChangeSets(changeSetsContent);
    }

    @Given("the main XML changelog file contains")
    public void mainChangelogFileContainsChangeSets(String changelogContent) throws IOException {
        mainChangelogFileWithNameContainsChangeSets("changelog.xml", changelogContent);
    }

    @Given("the main XML changelog file - named {string} - contains")
    public void mainChangelogFileWithNameContainsChangeSets(String changelogFileName, String changelogContent)
        throws IOException {
        databaseChangeLog = changelogFileName;
        changelogFileWithNameContainsChanges(changelogFileName, changelogContent);
    }

    @Given("the changelog file named {string} contains")
    public void changelogFileWithNameContainsChanges(String changelogFileName, String changelogContent)
        throws IOException {
        Path changelogPath = tempDirectory.resolve(changelogFileName);
        String changelog =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<databaseChangeLog xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\"\n" +
            "                   xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "                   xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog\n" +
            "                   http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.8.xsd\">\n" +
            changelogContent +
            "\n" +
            "</databaseChangeLog>";
        Files.write(changelogPath, changelog.getBytes());
    }

    @When("liquibase-linter runs")
    public void runliquibaseLinter() throws LiquibaseException {
        DatabaseChangeLog changelog = loadDatabaseChangelog(databaseChangeLog);
        ChangeLogLinter linter = initLinterFromConfigFile(configFile);

        try {
            linter.lintChangeLog(changelog);
        } catch (ChangeLogLintingException changeLogLintingException) {
            actualErrorMessage = Optional.of(changeLogLintingException.getMessage());
        }
    }

    private DatabaseChangeLog loadDatabaseChangelog(String changeLogFile) throws LiquibaseException {
        DatabaseConnection databaseConnection = new OfflineConnection("offline:h2", tempFilesResourceAccessor());
        Liquibase liquibase = new Liquibase(changeLogFile, tempFilesResourceAccessor(), databaseConnection);
        return liquibase.getDatabaseChangeLog();
    }

    @Then("no violation is detected")
    public void noViolationDetected() {
        assertThat(actualErrorMessage).isEmpty();
    }

    @Then("a violation is detected with the following message")
    public void violationDetectedWithMessage(String expectedErrorMessage) {
        assertThat(this.actualErrorMessage).hasValueSatisfying(message ->
            assertThat(message).contains(expectedErrorMessage)
        );
    }

    private ChangeLogLinter initLinterFromConfigFile(String configFile) {
        Config config = ConfigLoader.loadConfig(tempFilesResourceAccessor(), configFile);
        return new ChangeLogLinter(tempFilesResourceAccessor(), config);
    }

    private ResourceAccessor tempFilesResourceAccessor() {
        try {
            return new DirectoryResourceAccessor(tempDirectory.toFile());
        } catch (FileNotFoundException exception) {
            throw new AssertionError("Failed to find integration test resources", exception);
        }
    }
}
