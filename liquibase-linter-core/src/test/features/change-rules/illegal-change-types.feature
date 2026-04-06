Feature: Rule "illegal-change-types"

  Scenario: Should not allow an illegal change type by full class name
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "illegal-change-types": {
            "values": ["liquibase.change.core.LoadDataChange"]
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <loadData tableName="TEST" file="some-data.csv" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Change type 'liquibase.change.core.LoadDataChange' is not allowed in this project
      """

  Scenario: Should not allow an illegal change type by simple name
    Given liquibase-linter is configured with
      """json
      {
        "fail-fast": true,
        "rules": {
          "illegal-change-types": {
            "values": ["loadData"]
          }
        }
      }
      """
    And the main XML changelog file contains changes
      """xml
      <loadData tableName="TEST" file="some-data.csv" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Change type 'liquibase.change.core.LoadDataChange' is not allowed in this project
      """
