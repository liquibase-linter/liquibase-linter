Feature: Rule "table-name-length"

  Background:
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "table-name-length": {
            "maxLength": 26,
            "errorMessage": "Table '%s' name must not be longer than %d"
          }
        }
      }
      """

  Scenario: Should fail when table name length is exceeded
    Given the main XML changelog file contains changes
      """xml
      <createTable tableName="THIS_TABLE_NAME_IS_FAR_TOO_LONG" remarks="Some remarks">
        <column name="TEST" type="NVARCHAR" remarks="Some remarks">
          <constraints nullable="true" />
        </column>
      </createTable>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Table 'THIS_TABLE_NAME_IS_FAR_TOO_LONG' name must not be longer than 26
      """

  Scenario: Should pass when table name length is not exceeded
    Given the main XML changelog file contains changes
      """xml
      <createTable tableName="THIS_TABLE_NAME_SHORTER" remarks="Some remarks">
        <column name="TEST" type="NVARCHAR" remarks="Some remarks">
          <constraints nullable="true" />
        </column>
      </createTable>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should fail when table name length is exceeded on rename
    Given the main XML changelog file contains changes
      """xml
      <renameTable oldTableName="TEST" newTableName="THIS_TABLE_NAME_IS_FAR_TOO_LONG" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Table 'THIS_TABLE_NAME_IS_FAR_TOO_LONG' name must not be longer than 26
      """
