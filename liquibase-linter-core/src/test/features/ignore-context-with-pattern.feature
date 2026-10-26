Feature: It's possible to ignore some changesets with global "ignore-context-pattern" configuration

  Background:
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "ignore-context-pattern": "^baseline.*$",
        "rules": {
          "has-comment": true
        }
      }
      """

  Scenario: Should not report violation if one of the changeset contexts matches ignore pattern
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="tester" context="test, baseline-data">
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should detect violation if no changeset context matches ignore pattern
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="tester" context="test">
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Change set must have a comment
      """

  Scenario: Should detect violation if changeset has no context
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
      Change set must have a comment
      """
