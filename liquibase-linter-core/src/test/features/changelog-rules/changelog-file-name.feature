Feature: Rule "changelog-file-name"

  Background:
    Given rule "changelog-file-name" is enabled with the configuration
      | pattern | ^[^ ]+$ |

  Scenario: Invalid main changeLog filename
    Given the main XML changelog file - named "filename with-space.xml" - contains
      """
      <changeSet id="1730495746920-1" author="liquibase-linter-tests" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      File name: filename with-space.xml -- Message: ChangeLog filename 'filename with-space.xml' must follow pattern '^[^ ]+$'
      """

  Scenario: Invalid included changeLog filename
    Given the main XML changelog file - named "master.xml" - contains
      """
      <include relativeToChangelogFile="true" file="filename with-space.xml" />
      """
    And the changelog file named "filename with-space.xml" contains
      """
      <changeSet id="1730495746920-1" author="liquibase-linter-tests" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      File name: filename with-space.xml -- Message: ChangeLog filename 'filename with-space.xml' must follow pattern '^[^ ]+$'
      """

  Scenario: Valid changeLog filename
    Given the main XML changelog file - named "filename-with-no-spaces.xml" - contains
      """
      <changeSet id="1730495746920-1" author="liquibase-linter-tests" />
      """
    When liquibase-linter runs
    Then no violation is detected
