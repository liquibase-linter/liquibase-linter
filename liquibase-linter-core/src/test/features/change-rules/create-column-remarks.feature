Feature: Rule "create-column-remarks"

  Background:
    Given rule "create-column-remarks" is enabled

  Scenario: Should fail when add column does not have populated remarks attribute
    Given the main XML changelog file contains changes
      """xml
      <addColumn tableName="TEST">
        <column name="TEST_COLUMN" type="NVARCHAR(00)" />
      </addColumn>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Add column 'TEST_COLUMN' must contain remarks
      """

  Scenario: Should pass when add column has populated remarks attribute
    Given the main XML changelog file contains changes
      """xml
      <addColumn tableName="TEST">
        <column name="TEST_COLUMN" type="NVARCHAR(00)" remarks="some remarks" />
      </addColumn>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should fail when create table column does not have populated remarks attribute
    Given the main XML changelog file contains changes
      """xml
      <createTable tableName="TEST">
        <column name="TEST_COLUMN" type="NVARCHAR(00)" />
      </createTable>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Add column 'TEST_COLUMN' must contain remarks
      """
