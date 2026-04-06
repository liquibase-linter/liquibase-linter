Feature: Rule "unique-constraint-name"

  Background:
    Given rule "unique-constraint-name" is enabled with the configuration
      | errorMessage | Unique constraint '%s' must follow pattern table name followed by 'U' and a number e.g. TABLE_U1 |
      | pattern      | ^{{value}}_U[0-9]+$                                                                              |
      | condition    | (change.tableName + '_U').length() <= 28                                                         |
      | dynamicValue | tableName                                                                                        |

  Scenario: Should fail when unique constraint name does not match the pattern
    Given the main XML changelog file contains changes
      """xml
      <addUniqueConstraint tableName="TABLE_NAME" columnNames="COLUMN_NAME" constraintName="TABLE_NAME" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Unique constraint 'TABLE_NAME' must follow pattern table name followed by 'U' and a number e.g. TABLE_U1
      """

  Scenario: Should pass when unique constraint name matches the pattern
    Given the main XML changelog file contains changes
      """xml
      <addUniqueConstraint tableName="TABLE_NAME" columnNames="COLUMN_NAME" constraintName="TABLE_NAME_U1" />
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should pass when unique constraint name does not match the pattern but the condition is not met
    Given the main XML changelog file contains changes
      """xml
      <addUniqueConstraint
        tableName="THIS_TABLE_NAME_IS_FAR_TOO_LONG"
        columnNames="COLUMN_NAME"
        constraintName="THIS_TABLE_NAME_IS_FAR_TOO_LONG"
      />
      """
    When liquibase-linter runs
    Then no violation is detected
