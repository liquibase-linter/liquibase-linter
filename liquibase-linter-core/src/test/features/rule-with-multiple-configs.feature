Feature: A rule can be configured with multiple configurations

  Background:
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "object-name": [
            {
              "pattern": "^(?!_)[A-Z_0-9]+(?<!_)$",
              "errorMessage": "Object name '%s' name must be uppercase and use '_' separation"
            },
            {
              "pattern": "^POWER.*$",
              "errorMessage": "Object name '%s' name must begin with 'POWER'"
            }
          ]
        }
      }
      """

  Scenario: Should fail on the uppercase etc pattern
    Given the main XML changelog file contains changes
      """xml
      <addColumn tableName="TEST">
        <column name="nope" type="NVARCHAR(00)" />
      </addColumn>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Object name 'nope' name must be uppercase and use '_' separation
      """

  Scenario: Should fail on the prefix pattern
    Given the main XML changelog file contains changes
      """xml
      <addColumn tableName="TEST">
        <column name="NOPE" type="NVARCHAR(00)" />
      </addColumn>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Object name 'NOPE' name must begin with 'POWER'
      """

  Scenario: Should pass with multiple configs if they all pass
    Given the main XML changelog file contains changes
      """xml
      <addColumn tableName="TEST">
        <column name="POWER_THINGY" type="NVARCHAR(00)" />
      </addColumn>
      """
    When liquibase-linter runs
    Then no violation is detected
