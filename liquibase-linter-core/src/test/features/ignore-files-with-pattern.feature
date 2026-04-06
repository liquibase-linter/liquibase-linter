Feature: It's possible to ignore some changelog files with global "ignore-files-pattern" configuration

  Scenario: Should detect violation if file is not ignored
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "has-comment": true
        }
      }
      """
    And the main XML changelog file contains changesets
      """xml
      <include relativeToChangelogFile="true" file="ignore/has-comment-fail.xml" />
      """
    And the additional XML changelog file "ignore/has-comment-fail.xml" contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers">
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

  Scenario: Should not report violation if changelog file matches ignore pattern
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "ignore-files-pattern": "^ignore/.*$",
        "rules": {
          "has-comment": true
        }
      }
      """
    And the main XML changelog file contains changesets
      """xml
      <include relativeToChangelogFile="true" file="ignore/has-comment-fail.xml" />
      """
    And the additional XML changelog file "ignore/has-comment-fail.xml" contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers">
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then no violation is detected
