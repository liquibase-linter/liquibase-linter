Feature: Rule "column-type"

  Rule: The "column-type" rule can be used to enforce that column types follow a specified pattern.
    The pattern is a regular expression that is applied to the column type.

    Scenario: Should pass when column name matches the pattern
      Given rule "column-type" is enabled with the configuration
        | pattern | ^varchar2\(30\)$ |
      And the main XML changelog file contains changes
        """xml
        <addColumn tableName="TEST_TABLE">
          <column name="reference" type="varchar2(30)" />
        </addColumn>
        """
      When liquibase-linter runs
      Then no violation is detected

    Scenario: Should fail when column name does not match the pattern
      Given rule "column-type" is enabled with the configuration
        | pattern | ^varchar2\(30\)$ |
      And the main XML changelog file contains changes
        """xml
        <addColumn tableName="TEST_TABLE">
          <column name="reference" type="varchar2(50)" />
        </addColumn>
        """
      When liquibase-linter runs
      Then a violation is detected with the following message
        """
        Type 'varchar2(50)' of column 'reference' does not follow pattern '^varchar2\(30\)$
        """

  Rule: A "columnCondition" can be used to specify that the rule should only be applied to columns matching the condition.
    The condition is a SpEL expression that can reference column properties (e.g., name, type)

    Scenario: A violation is detected when column matches the columnCondition and does not match the pattern
      Given rule "column-type" is enabled with the configuration
        | pattern         | ^varchar2\(30\)$    |
        | columnCondition | name == 'reference' |
      And the main XML changelog file contains changes
        """xml
        <addColumn tableName="TEST_TABLE">
          <column name="reference" type="varchar2(50)" />
        </addColumn>
        """
      When liquibase-linter runs
      Then a violation is detected with the following message
        """
        Type 'varchar2(50)' of column 'reference' does not follow pattern '^varchar2\(30\)$
        """

    Scenario: No violation is detected when column does not match the pattern but does not satisfies the columnCondition
      Given rule "column-type" is enabled with the configuration
        | pattern         | ^varchar2\(30\)$    |
        | columnCondition | name == 'reference' |
      And the main XML changelog file contains changes
        """xml
        <addColumn tableName="TEST_TABLE">
          <column name="other_column" type="varchar2(50)" />
        </addColumn>
        """
      When liquibase-linter runs
      Then no violation is detected
