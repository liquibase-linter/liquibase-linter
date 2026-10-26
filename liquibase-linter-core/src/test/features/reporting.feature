Feature: Enabled reporters are run once linting is done

  Background:
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201808283523452" author="liquibase-linter">
        <sql>SELECT 1 FROM DUAL</sql>
      </changeSet>
      """

  Scenario: Should write report files of enabled file reporters only
    Given liquibase-linter is configured with
      """json
      {
        "rules": {
          "has-comment": true
        },
        "reporting": {
          "text": "${testDirectory}/report.txt",
          "markdown": {
            "path": "${testDirectory}/report.md",
            "enabled": false
          }
        }
      }
      """
    When liquibase-linter runs
    Then the report file "report.txt" contains
      """
      'has-comment': Change set must have a comment
      """
    And the report file "report.md" does not exist

  Scenario: Should print report with console reporter
    Given liquibase-linter is configured with
      """json
      {
        "rules": {
          "has-comment": true
        },
        "reporting": {
          "console": true
        }
      }
      """
    When liquibase-linter runs
    Then the console output contains
      """
      'has-comment': Change set must have a comment
      """

  Scenario: Should not print report with disabled console reporter
    Given liquibase-linter is configured with
      """json
      {
        "rules": {
          "has-comment": true
        },
        "reporting": {
          "console": false
        }
      }
      """
    When liquibase-linter runs
    Then the console output is empty
