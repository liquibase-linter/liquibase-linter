Feature: Rule "modify-data-starts-with-where"

  Background:
    Given rule "modify-data-starts-with-where" is enabled

  Scenario: Should fail when modify data where condition starts with 'where'
    Given the main XML changelog file contains changes
      """xml
      <update tableName="MUST_HAVE_WHERE">
        <column name="TESTR" value="123" />
        <where>where ABC = 'ABC'</where>
      </update>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Modify data where starts with where clause, that's probably a mistake
      """

  Scenario: Should pass when modify data where condition does not start with 'where'
    Given the main XML changelog file contains changes
      """xml
      <update tableName="MUST_HAVE_WHERE">
        <column name="TESTR" value="123" />
        <where>ABC = 'ABC'</where>
      </update>
      """
    When liquibase-linter runs
    Then no violation is detected
