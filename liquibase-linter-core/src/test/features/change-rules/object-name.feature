Feature: Rule "object-name"

  Background:
    Given rule "object-name" is enabled with the configuration
      | pattern      | ^(?!_)[A-Z_0-9]+(?<!_)$                                        |
      | errorMessage | Object name '%s' name must be uppercase and use '_' separation |

  Scenario: Should fail when object name does not match the pattern
    Given the main XML changelog file contains changes
      """xml
      <addColumn tableName="TEST">
        <column name="NOT%FOLLOWING%PATTERN" type="NVARCHAR(00)" />
      </addColumn>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Object name 'NOT%FOLLOWING%PATTERN' name must be uppercase and use '_' separation
      """

  Scenario: Should pass when object name matches the pattern
    Given the main XML changelog file contains changes
      """xml
      <addColumn tableName="TEST">
        <column name="OBJECT_NAME" type="NVARCHAR(00)" />
      </addColumn>
      """
    When liquibase-linter runs
    Then no violation is detected
