Feature: Rule "no-preconditions"

  Background:
    Given rule "no-preconditions" is enabled

  Scenario: Should not allow preconditions at changeset level
    Given the main XML changelog file contains changesets
      """xml
      <changeSet id="201809011238" author="luke.rogers">
        <preConditions>
          <dbms type="oracle" />
        </preConditions>
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Preconditions are not allowed in this project
      """

  Scenario: Should not allow preconditions at changelog level
    Given the main XML changelog file contains changesets
      """xml
      <preConditions>
        <dbms type="oracle" />
      </preConditions>

      <changeSet id="201809011238" author="luke.rogers">
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
      </changeSet>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Preconditions are not allowed in this project
      """
