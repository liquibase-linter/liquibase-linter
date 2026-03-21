Feature: Rule "create-table-remarks"

  Background:
    Given rule "create-table-remarks" is enabled

  Scenario: Should fail when create table does not have populated remarks attribute
    Given the main XML changelog file contains changes
      """xml
      <createTable tableName="TEST">
        <column name="TEST" type="NVARCHAR" remarks="Some remarks">
          <constraints nullable="true" />
        </column>
      </createTable>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Create table must contain remark attribute
      """

  Scenario: Should pass when create table has populated remarks attribute
    Given the main XML changelog file contains changes
      """xml
      <createTable tableName="TEST" remarks="Some remarks">
        <column name="TEST" type="NVARCHAR" remarks="Some remarks">
          <constraints nullable="true" />
        </column>
      </createTable>
      """
    When liquibase-linter runs
    Then no violation is detected
