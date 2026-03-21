Feature: Rule "create-column-no-define-primary-key"

  Background:
    Given rule "create-column-no-define-primary-key" is enabled

  Scenario: Should fail when add column specifies primary key constraint attribute
    Given the main XML changelog file contains changes
      """xml
      <addColumn tableName="TEST">
        <column name="TEST_COLUMN" type="NVARCHAR(00)">
          <constraints primaryKey="true" />
        </column>
      </addColumn>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Add column must not use primary key attribute. Instead use AddPrimaryKey change type
      """

  Scenario: Should pass when add column does not specify primary key constraint attribute
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

  Scenario: Should fail when create table column specifies primary key constraint attribute
    Given the main XML changelog file contains changes
      """xml
      <createTable tableName="TEST">
        <column name="TEST_COLUMN" type="NVARCHAR(00)">
          <constraints primaryKey="true" />
        </column>
      </createTable>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Add column must not use primary key attribute. Instead use AddPrimaryKey change type
      """
