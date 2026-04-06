Feature: Rule "separate-ddl-context"

  Background:
    Given rule "separate-ddl-context" is enabled with the configuration
      | pattern      | ^.*ddl_test\|.*ddl_script$                   |
      | errorMessage | Should have a ddl changes under ddl contexts |

  Scenario: Should fail with ddl script under non ddl context
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
      Should have a ddl changes under ddl contexts
      """

  Scenario: Should pass ddl script under ddl context
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers" context="ddl_script">
        <addColumn tableName="TEST_TABLE">
          <column name="TEST_COLUMN_1" type="NVARCHAR(100)" />
        </addColumn>
      </changeSet>
      """
    When liquibase-linter runs
    Then no violation is detected
