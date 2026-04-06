Feature: Rule "table-name"

  Background:
    Given rule "table-name" is enabled with the configuration
      | pattern      | ^(?!TBL)[A-Z_]+(?<!_)$                                                       |
      | errorMessage | Table '%s' name must be uppercase, use '_' separation and not start with TBL |

  Scenario: Should fail when table name does not match the pattern
    Given the main XML changelog file contains changes
      """xml
      <createTable tableName="TBL_TEST" remarks="Some remarks">
        <column name="TEST" type="NVARCHAR" remarks="Some remarks">
          <constraints nullable="true" />
        </column>
      </createTable>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Table 'TBL_TEST' name must be uppercase, use '_' separation and not start with TBL
      """

  Scenario: Should pass when table name matches the pattern
    Given the main XML changelog file contains changes
      """xml
      <createTable tableName="VALID_TABLE_NAME" remarks="Some remarks">
        <column name="TEST" type="NVARCHAR" remarks="Some remarks">
          <constraints nullable="true" />
        </column>
      </createTable>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should fail when table name does not match the pattern on rename
    Given the main XML changelog file contains changes
      """xml
      <renameTable oldTableName="TEST" newTableName="TBL_TEST" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Table 'TBL_TEST' name must be uppercase, use '_' separation and not start with TBL
      """
