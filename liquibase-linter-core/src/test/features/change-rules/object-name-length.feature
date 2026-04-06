Feature: Rule "object-name-length"

  Background:
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "object-name-length": {
            "maxLength": 30,
            "errorMessage": "Object name '%s' must be less than %d characters"
          }
        }
      }
      """

  Scenario: Should fail when object name length is exceeded
    Given the main XML changelog file contains changes
      """xml
      <addColumn tableName="TEST">
        <column name="THIS_OBJECT_NAME_IS_FAR_TOO_LONG" type="NVARCHAR(00)" />
      </addColumn>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Object name 'THIS_OBJECT_NAME_IS_FAR_TOO_LONG' must be less than 30 characters
      """

  Scenario: Should pass when object name length is not exceeded
    Given the main XML changelog file contains changes
      """xml
      <addColumn tableName="TEST">
        <column name="OBJECT_NAME_IS_SHORTER" type="NVARCHAR(00)" />
      </addColumn>
      """
    When liquibase-linter runs
    Then no violation is detected
