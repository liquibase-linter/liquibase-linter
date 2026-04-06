Feature: A specific rule can be ignored using a comment starting with "lql-ignore"

  Scenario: Should be allowed to ignore specific rules
    Given rule "table-name-length" is enabled with the configuration
      | maxLength | 10 |
    And the main XML changelog file contains changesets
      """xml
      <changeSet id="201809061558" author="tester">
        <comment>lql-ignore:table-name-length</comment>
        <createTable tableName="THIS_NAME_SHOULD_BE_TOO_LONG">
          <column name="TEST" type="VARCHAR(10)" />
        </createTable>
      </changeSet>
      """
    When liquibase-linter runs
    Then no violation is detected
