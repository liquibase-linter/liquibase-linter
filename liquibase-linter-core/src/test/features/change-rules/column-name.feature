Feature: Rule "column-name"

  Background:
    Given rule "column-name" is enabled with the configuration
      | pattern      | ^[a-z_]+$                              |
      | errorMessage | Column name '%s' should be lower cased |

  Scenario: Column name not matching the configured pattern
    Given the main XML changelog file contains changes
      """
      <renameColumn oldColumnName="TEST" newColumnName="INVALID_UPPERCASED_COLUMN_NAME" tableName="TEST_TABLE" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Column name 'INVALID_UPPERCASED_COLUMN_NAME' should be lower cased
      """

  Scenario: Column name matching the configured pattern
    Given the main XML changelog file contains changes
      """
      <renameColumn oldColumnName="TEST" newColumnName="valid_lowercased_column_name" tableName="TEST_TABLE" />
      """
    When liquibase-linter runs
    Then no violation is detected
