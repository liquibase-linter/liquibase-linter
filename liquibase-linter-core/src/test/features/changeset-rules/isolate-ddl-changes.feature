Feature: Rule "isolate-ddl-changes"

  Background:
    Given rule "isolate-ddl-changes" is enabled

  Scenario: Should fail with more than one DDL change within a single changeset
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers">
        <addColumn tableName="TEST_TABLE">
          <column name="TEST_COLUMN_1" type="NVARCHAR(100)" />
        </addColumn>
        <addColumn tableName="TEST_TABLE">
          <column name="TEST_COLUMN_2" type="NVARCHAR(100)" />
        </addColumn>
      </changeSet>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Should only have a single ddl change per change set
      """

  Scenario: Should pass with a single DDL change within a single changeset
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers">
        <addColumn tableName="TEST_TABLE">
          <column name="TEST_COLUMN_1" type="NVARCHAR(100)" />
        </addColumn>
      </changeSet>
      """
    When liquibase-linter runs
    Then no violation is detected
