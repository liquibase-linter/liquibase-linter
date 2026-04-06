Feature: Rule "foreign-key-name"

  Background:
    Given rule "foreign-key-name" is enabled with the configuration
      | errorMessage | Foreign key constraint '%s' must be named, ending in _FK, and follow pattern '{base_table_name}_{parent_table_name}_FK' where space permits |
      | pattern      | ^{{value}}_FK$                                                                                                                              |
      | dynamicValue | (baseTableName + referencedTableName + '_FK').length() <= 15 ? baseTableName + '_' + referencedTableName : '[A-Z_]+'                        |

  Scenario: Should fail when the contextual naming can't be enforced but the suffix isn't used
    Given the main XML changelog file contains changes
      """xml
      <addForeignKeyConstraint
        referencedTableName="FOO"
        baseTableName="BAR"
        referencedColumnNames="ID"
        baseColumnNames="FOO_ID"
        constraintName="NOT_EVEN_CLOSE"
      />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Foreign key constraint 'NOT_EVEN_CLOSE' must be named, ending in _FK, and follow pattern '{base_table_name}_{parent_table_name}_FK' where space permits
      """

  Scenario: Should fail when the contextual naming can be enforced and isn't used
    Given the main XML changelog file contains changes
      """xml
      <addForeignKeyConstraint
        referencedTableName="FOO"
        baseTableName="BAR"
        referencedColumnNames="ID"
        baseColumnNames="FOO_ID"
        constraintName="WHOOPS_FK"
      />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Foreign key constraint 'WHOOPS_FK' must be named, ending in _FK, and follow pattern '{base_table_name}_{parent_table_name}_FK' where space permits
      """

  Scenario: Should pass when used correctly
    Given the main XML changelog file contains changes
      """xml
      <addForeignKeyConstraint
        referencedTableName="THIS_IS_TOO_LONG"
        baseTableName="BAR"
        referencedColumnNames="ID"
        baseColumnNames="FOO_ID"
        constraintName="SOMETHING_FK"
      />
      <addForeignKeyConstraint
        referencedTableName="FOO"
        baseTableName="BAR"
        referencedColumnNames="ID"
        baseColumnNames="FOO_ID"
        constraintName="BAR_FOO_FK"
      />
      """
    When liquibase-linter runs
    Then no violation is detected
