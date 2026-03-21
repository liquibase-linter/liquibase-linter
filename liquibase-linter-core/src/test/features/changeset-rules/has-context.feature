Feature: Rule "has-context"

  Background:
    Given rule "has-context" is enabled

  Scenario: Should not pass with no context value
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="tester">
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Should have at least one context on the change set
      """

  Scenario: Should pass with a context value
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="tester" context="foo">
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then no violation is detected
