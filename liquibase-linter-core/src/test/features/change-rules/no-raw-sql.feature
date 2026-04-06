Feature: Rule "no-raw-sql"

  Background:
    Given rule "no-raw-sql" is enabled

  Scenario: Should not allow raw sql change type
    Given the main XML changelog file contains changes
      """xml
      <sql>SELECT 1 FROM DUAL</sql>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Raw sql change types are not allowed, use appropriate Liquibase change types
      """

  Scenario: Should not allow sql file change type
    Given the main XML changelog file contains changes
      """xml
      <sqlFile path="raw.sql" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Raw sql change types are not allowed, use appropriate Liquibase change types
      """

  Scenario: Should allow non raw sql change type
    Given the main XML changelog file contains changes
      """xml
      <addColumn tableName="TEST">
        <column name="COL" type="varchar(100)" />
      </addColumn>
      """
    When liquibase-linter runs
    Then no violation is detected
