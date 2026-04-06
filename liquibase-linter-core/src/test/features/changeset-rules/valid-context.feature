Feature: Rule "valid-context"

  Background:
    Given rule "valid-context" is enabled with the configuration
      | pattern      | ^.*_test\|.*_script$                                       |
      | errorMessage | Context is incorrect, should end with '_test' or '_script' |

  Scenario: Should fail with invalid context value
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers" context="invalid-context">
        <addColumn tableName="TEST_TABLE">
          <column name="TEST_COLUMN_1" type="NVARCHAR(100)" />
        </addColumn>
      </changeSet>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Context is incorrect, should end with '_test' or '_script'
      """

  Scenario: Should pass with valid context value
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers" context="valid-context_script">
        <addColumn tableName="TEST_TABLE">
          <column name="TEST_COLUMN_1" type="NVARCHAR(100)" />
        </addColumn>
      </changeSet>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should pass with no context value
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
