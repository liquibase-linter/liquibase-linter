Feature: All rules can be configured with a condition to apply only on specific contextes

  Scenario: Should match simple context and fail rule
    Given rule "has-comment" is enabled with the configuration
      | condition | matchesContext('foo') |
    And the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers" context="foo">
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Change set must have a comment
      """

  Scenario: Should not match negative simple context and pass rule
    Given rule "has-comment" is enabled with the configuration
      | condition | matchesContext('foo') |
    And the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers" context="!foo">
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should not match simple context mismatch and pass rule
    Given rule "has-comment" is enabled with the configuration
      | condition | matchesContext('bar') |
    And the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers" context="foo">
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should not match no context and pass rule
    Given rule "has-comment" is enabled with the configuration
      | condition | matchesContext('foo') |
    And the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers">
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should match multiple contexts and fail rule
    Given rule "has-comment" is enabled with the configuration
      | condition | matchesContext('foo', 'bar') |
    And the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers" context="foo and bar">
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Change set must have a comment
      """

  Scenario: Should match multiple contexts with not and fail rule
    Given rule "has-comment" is enabled with the configuration
      | condition | matchesContext('foo', '!bar') |
    And the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers" context="foo and !bar">
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Change set must have a comment
      """
