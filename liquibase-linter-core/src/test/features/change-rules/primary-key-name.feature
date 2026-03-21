Feature: Rule "primary-key-name"

  Scenario: Should fail when the table name can't be enforced but the suffix isn't used
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "primary-key-name": {
            "errorMessage": "Primary key constraint '%s' must be named, ending with '_PK', and start with table name (unless too long)",
            "pattern": "^{{value}}_PK$",
            "dynamicValue": "(tableName + '_PK').length() <= 10 ? tableName : '[A-Z_]+'"
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <addPrimaryKey tableName="FOO_NAME_TOO_LONG" columnNames="BAR" constraintName="NOT_EVEN_CLOSE" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Primary key constraint 'NOT_EVEN_CLOSE' must be named, ending with '_PK', and start with table name (unless too long)
      """

  Scenario: Should fail when the table name can be enforced and isn't used
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "primary-key-name": {
            "errorMessage": "Primary key constraint '%s' must be named, ending with '_PK', and start with table name (unless too long)",
            "pattern": "^{{value}}_PK$",
            "dynamicValue": "(tableName + '_PK').length() <= 10 ? tableName : '[A-Z_]+'"
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <addPrimaryKey tableName="FOO" columnNames="BAR" constraintName="BAZ_PK" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Primary key constraint 'BAZ_PK' must be named, ending with '_PK', and start with table name (unless too long)
      """

  Scenario: Should fail when the table name can be enforced and isn't used on create table
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "primary-key-name": {
            "errorMessage": "Primary key constraint '%s' must be named, ending with '_PK', and start with table name (unless too long)",
            "pattern": "^{{value}}_PK$",
            "dynamicValue": "(tableName + '_PK').length() <= 10 ? tableName : '[A-Z_]+'"
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <createTable tableName="FOO">
        <column name="BAR">
          <constraints primaryKey="true" primaryKeyName="BAZ_PK" />
        </column>
      </createTable>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Primary key constraint 'BAZ_PK' must be named, ending with '_PK', and start with table name (unless too long)
      """

  Scenario: Should pass when used correctly
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "primary-key-name": {
            "errorMessage": "Primary key constraint '%s' must be named, ending with '_PK', and start with table name (unless too long)",
            "pattern": "^{{value}}_PK$",
            "dynamicValue": "(tableName + '_PK').length() <= 10 ? tableName : '[A-Z_]+'"
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <addPrimaryKey tableName="FOO_NAME_TOO_LONG" columnNames="BAR" constraintName="FOO_NTL_PK" />
      <addPrimaryKey tableName="FOO" columnNames="BAR" constraintName="FOO_PK" />
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should fail when constraint name is omitted with simple config
    Given rule "primary-key-name" is enabled
    And the main XML changelog file contains changes
      """xml
      <addPrimaryKey tableName="FOO_NAME_TOO_LONG" columnNames="BAR" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Primary key name '' is missing or does not follow pattern
      """

  Scenario: Should fail when constraint name is omitted with complex config
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "primary-key-name": {
            "errorMessage": "Primary key constraint '%s' must be named, ending with '_PK', and start with table name (unless too long)",
            "pattern": "^{{value}}_PK$",
            "dynamicValue": "(tableName + '_PK').length() <= 10 ? tableName : '[A-Z_]+'"
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <addPrimaryKey tableName="FOO_NAME_TOO_LONG" columnNames="BAR" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Primary key constraint '' must be named, ending with '_PK', and start with table name (unless too long)
      """
