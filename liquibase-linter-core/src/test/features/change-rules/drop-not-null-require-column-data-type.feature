Feature: Rule "drop-not-null-require-column-data-type"

  Background:
    Given rule "drop-not-null-require-column-data-type" is enabled

  Scenario: Should fail with invalid context value
    Given the main XML changelog file contains changes
      """xml
      <dropNotNullConstraint tableName="TABLE_NAME" columnName="COLUMN_NAME" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Drop not null constraint column data type attribute must be populated
      """

  Scenario: Should pass with valid context value
    Given the main XML changelog file contains changes
      """xml
      <dropNotNullConstraint tableName="TABLE_NAME" columnName="COLUMN_NAME" columnDataType="NVARCHAR(100)" />
      """
    When liquibase-linter runs
    Then no violation is detected
