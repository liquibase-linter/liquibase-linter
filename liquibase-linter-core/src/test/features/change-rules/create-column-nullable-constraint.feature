Feature: Rule "create-column-nullable-constraint"

  Background:
    Given rule "create-column-nullable-constraint" is enabled

  Scenario: Should fail when add column does not have a constraints tag with a populated nullable attribute
    Given the main XML changelog file contains changes
      """xml
      <addColumn tableName="TEST">
        <column name="TEST_COLUMN" type="NVARCHAR(00)" />
      </addColumn>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Add column 'TEST_COLUMN' must specify nullable constraint
      """

  Scenario: Should pass when add column has a constraints tag with a populated nullable attribute
    Given the main XML changelog file contains changes
      """xml
      <addColumn tableName="TEST">
        <column name="TEST_COLUMN" type="NVARCHAR(00)">
          <constraints nullable="true" />
        </column>
      </addColumn>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should fail when create table column does not have a constraints tag with a populated nullable attribute
    Given the main XML changelog file contains changes
      """xml
      <createTable tableName="TEST">
        <column name="TEST_COLUMN" type="NVARCHAR(00)" />
      </createTable>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Add column 'TEST_COLUMN' must specify nullable constraint
      """
