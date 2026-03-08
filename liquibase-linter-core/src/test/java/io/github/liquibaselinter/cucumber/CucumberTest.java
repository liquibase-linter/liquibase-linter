package io.github.liquibaselinter.cucumber;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectDirectories;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SuppressWarnings("java:S2187")
@SelectDirectories("src/test/features")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "io.github.liquibaselinter")
@ConfigurationParameter(
    key = PLUGIN_PROPERTY_NAME,
    value = "pretty, html:target/cucumber/cucumber.htm, junit:target/cucumber/TEST-cucumber.xml"
)
public class CucumberTest {}
