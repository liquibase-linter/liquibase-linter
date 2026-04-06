Feature: Rule "index-name"

  Background:
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "index-name": {
            "errorMessage": "Index '%s' must follow pattern table name followed by 'I' and a number e.g. APPLICATION_I1, or match a primary key or unique constraint name",
            "pattern": "^{{value}}_(PK|U\\d|I\\d){1}$",
            "condition": "change.tableName.length() <= 26",
            "dynamicValue": "tableName"
          }
        }
      }
      """

  Scenario: Should fail when create index name does not match the pattern
    Given the main XML changelog file contains changes
      """xml
      <createIndex tableName="TABLE_NAME" indexName="TABLE_NAME">
        <column name="TEST" />
      </createIndex>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Index 'TABLE_NAME' must follow pattern table name followed by 'I' and a number e.g. APPLICATION_I1, or match a primary key or unique constraint name
      """

  Scenario: Should pass when create index name matches the pattern
    Given the main XML changelog file contains changes
      """xml
      <createIndex tableName="TABLE_NAME" indexName="TABLE_NAME_I1">
        <column name="TEST" />
      </createIndex>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should pass when create index name does not match the pattern but the condition is not met
    Given the main XML changelog file contains changes
      """xml
      <createIndex tableName="THIS_TABLE_NAME_IS_FAR_TOO_LONG" indexName="TABLE_NAME">
        <column name="TEST" />
      </createIndex>
      """
    When liquibase-linter runs
    Then no violation is detected
