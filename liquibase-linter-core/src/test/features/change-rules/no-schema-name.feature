Feature: Rule "no-schema-name"

  Background:
    Given rule "no-schema-name" is enabled

  Scenario: Should fail when schema name is present
    Given the main XML changelog file contains changes
      """xml
      <update schemaName="SCHEMA_NAME" tableName="TEST">
        <column name="TESTR" value="123" />
        <where>ABC = 'ABC'</where>
      </update>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Schema names are not allowed in this project
      """

  Scenario: Should pass when schema name is not present
    Given the main XML changelog file contains changes
      """xml
      <update tableName="TEST">
        <column name="TESTR" value="123" />
        <where>ABC = 'ABC'</where>
      </update>
      """
    When liquibase-linter runs
    Then no violation is detected
