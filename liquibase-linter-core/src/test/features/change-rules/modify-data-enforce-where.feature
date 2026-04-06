Feature: Rule "modify-data-enforce-where"

  Scenario: Should fail when modify data does not have where condition
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "modify-data-enforce-where": {
            "errorMessage": "Modify data on table '%s' must have a where condition",
            "values": ["MUST_HAVE_WHERE"]
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <update tableName="MUST_HAVE_WHERE">
        <column name="TESTR" value="123" />
      </update>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Modify data on table 'MUST_HAVE_WHERE' must have a where condition
      """

  Scenario: Should pass when modify data has where condition
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "modify-data-enforce-where": {
            "errorMessage": "Modify data on table '%s' must have a where condition",
            "values": ["MUST_HAVE_WHERE"]
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <update tableName="MUST_HAVE_WHERE">
        <column name="TESTR" value="123" />
        <where>ABC = 'ABC'</where>
      </update>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should fail when delete data does not have where condition
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "modify-data-enforce-where": {
            "errorMessage": "Modify data on table '%s' must have a where condition",
            "values": ["MUST_HAVE_WHERE"]
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <delete tableName="MUST_HAVE_WHERE" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Modify data on table 'MUST_HAVE_WHERE' must have a where condition
      """

  Scenario: Should pass when modify data does not require where condition
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "modify-data-enforce-where": {
            "errorMessage": "Modify data on table '%s' must have a where condition",
            "values": ["MUST_HAVE_WHERE"]
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <update tableName="ANOTHER_TABLE">
        <column name="TESTR" value="123" />
      </update>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should fail when modify data where condition does not match pattern
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "modify-data-enforce-where": {
            "errorMessage": "Modify data on table '%s' must have a where condition including with 'CODE = '",
            "values": ["MUST_HAVE_WHERE"],
            "pattern": "^.*CODE = .*$"
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <update tableName="MUST_HAVE_WHERE">
        <column name="TESTR" value="123" />
        <where>NOPE = 'NAH'</where>
      </update>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Modify data on table 'MUST_HAVE_WHERE' must have a where condition including with 'CODE = '
      """

  Scenario: Should fail when modify data where condition is missing when pattern provided
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "modify-data-enforce-where": {
            "errorMessage": "Modify data on table '%s' must have a where condition including with 'CODE = '",
            "values": ["MUST_HAVE_WHERE"],
            "pattern": "^.*CODE = .*$"
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <update tableName="MUST_HAVE_WHERE">
        <column name="TESTR" value="123" />
      </update>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Modify data on table 'MUST_HAVE_WHERE' must have a where condition including with 'CODE = '
      """

  Scenario: Should pass when modify data has where condition matching pattern
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "modify-data-enforce-where": {
            "errorMessage": "Modify data on table '%s' must have a where condition including with 'CODE = '",
            "values": ["MUST_HAVE_WHERE"],
            "pattern": "^.*CODE = .*$"
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <update tableName="MUST_HAVE_WHERE">
        <column name="TESTR" value="123" />
        <where>CODE = 'FOO'</where>
      </update>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should fail when delete data does not have where condition with regex matcher
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "modify-data-enforce-where": {
            "errorMessage": "Modify data on table '%s' must have a where condition",
            "values": [".*"]
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <delete tableName="MUST_HAVE_WHERE" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Modify data on table 'MUST_HAVE_WHERE' must have a where condition
      """
