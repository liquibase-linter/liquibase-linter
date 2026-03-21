Feature: Violations should be aggregated in the final report

  Scenario: Should aggregate errors
    Given liquibase-linter is configured with
      """json
      {
        "rules": {
          "no-preconditions": true,
          "has-comment": true,
          "has-context": true
        }
      }
      """
    And the main XML changelog file contains changesets
      """xml
      <changeSet id="201808283523452" author="liquibase-linter">
        <sql>SELECT 1 FROM DUAL</sql>
      </changeSet>

      <changeSet id="2017013452354" author="liquibase-linter" context="test">
        <comment>lql-ignore:has-comment</comment>
        <update tableName="TEST">
          <column name="TEST" value="TEST" />
        </update>
      </changeSet>

      <changeSet id="20170245245" author="liquibase-linter">
        <preConditions>
          <indexExists tableName="TEST" />
        </preConditions>
        <comment>lql-ignore:has-context</comment>
        <update tableName="TEST">
          <column name="TEST" value="TEST" />
        </update>
      </changeSet>
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Linting failed with 3 errors:
       - Change set must have a comment
       - Should have at least one context on the change set
       - Preconditions are not allowed in this project
      """
