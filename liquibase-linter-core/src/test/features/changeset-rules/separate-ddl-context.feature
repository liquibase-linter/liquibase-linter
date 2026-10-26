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

  Scenario: Should fail with dml script under ddl context
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers" context="ddl_script">
        <insert tableName="TEST_TABLE">
          <column name="TEST_COLUMN_1" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Should have a ddl changes under ddl contexts
      """

  Scenario: Should pass dml script under non ddl contexts
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers" context="test, data">
        <insert tableName="TEST_TABLE">
          <column name="TEST_COLUMN_1" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should pass changes that are neither ddl nor dml under any context
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers" context="invalid-context">
        <sql>SELECT 1</sql>
      </changeSet>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should pass changeset without context
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
