Feature: Rule "schema-name"

  Background:
    Given rule "schema-name" is enabled with the configuration
      | pattern      | ^\$\{[a-z_]+\}$                    |
      | errorMessage | Must use schema name token, not %s |

  Scenario: Should fail when schema name doesn't match pattern
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
      Must use schema name token, not SCHEMA_NAME
      """

  Scenario: Should pass when schema name matches pattern
    Given the main XML changelog file contains changes
      """xml
      <update schemaName="${schema_name}" tableName="TEST">
        <column name="TESTR" value="123" />
        <where>ABC = 'ABC'</where>
      </update>
      """
    When liquibase-linter runs
    Then no violation is detected
