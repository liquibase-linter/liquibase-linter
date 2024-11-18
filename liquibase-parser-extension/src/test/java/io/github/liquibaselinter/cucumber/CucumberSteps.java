package io.github.liquibaselinter.cucumber;

import com.google.common.io.CharStreams;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.liquibaselinter.ChangeLogLintingException;
import io.github.liquibaselinter.resolvers.LiquibaseIntegrationTestResolver;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.Scope;
import liquibase.exception.ChangeLogParseException;
import liquibase.exception.CommandExecutionException;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class CucumberSteps {

    private final Writer nullWriter = CharStreams.nullWriter();
    private final Contexts contexts = new Contexts();
    private Optional<String> actualErrorMessage;

    @Before
    public void resetErrorMessage() {
        actualErrorMessage = Optional.empty();
    }

    @When("I run the liquibase linter in directory {string} with")
    public void runLiquibaseLinter(String testDirectory, Map<String, String> parameters) throws Exception {
        String configFile = testDirectory + "/" + parameters.get("configuration");
        String changeLogFile = testDirectory + "/" + parameters.get("changelog");

        Scope.child(new HashMap<>(), () -> {
            Liquibase liquibase = LiquibaseIntegrationTestResolver.buildLiquibase(changeLogFile, configFile);
            try {
                liquibase.getDatabaseChangeLog();
            } catch (CommandExecutionException commandExecutionException) {
                if (commandExecutionException.getCause() instanceof ChangeLogParseException
                    && commandExecutionException.getCause().getCause() instanceof ChangeLogLintingException) {
                    actualErrorMessage = Optional.of(commandExecutionException.getCause().getCause().getMessage());
                } else {
                    throw new AssertionError("Unexpected exception", commandExecutionException);
                }
            } catch (Exception exception) {
                throw new AssertionError("Unexpected exception", exception);
            }
        });
    }

    @Then("No violation is detected")
    public void noViolationDetected() {
        assertThat(actualErrorMessage).isEmpty();
    }

    @Then("A violation is detected with the following message")
    public void violationDetectedWithMessage(String expectedErrorMessage) {
        assertThat(this.actualErrorMessage).hasValueSatisfying(message -> assertThat(message).contains(expectedErrorMessage));
    }
}
