Feature: Rule "sequence-name"

  Background:
    Given rule "sequence-name" is enabled with the configuration
      | pattern      | ^(?!SEQ)[A-Z_]+(?<!_)$                                                          |
      | errorMessage | Sequence '%s' name must be uppercase, use '_' separation and not start with SEQ |

  Scenario: Should fail when sequence name does not match the pattern
    Given the main XML changelog file contains changes
      """xml
      <createSequence sequenceName="SEQ_TEST" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Sequence 'SEQ_TEST' name must be uppercase, use '_' separation and not start with SEQ
      """

  Scenario: Should pass when sequence name matches the pattern
    Given the main XML changelog file contains changes
      """xml
      <createSequence sequenceName="VALID_SEQUENCE_NAME" />
      """
    When liquibase-linter runs
    Then no violation is detected

  Scenario: Should fail when sequence name does not match the pattern on rename
    Given the main XML changelog file contains changes
      """xml
      <renameSequence oldSequenceName="TEST" newSequenceName="SEQ_TEST" />
      """
    When liquibase-linter runs
    Then a violation is detected with the following message
      """
      Sequence 'SEQ_TEST' name must be uppercase, use '_' separation and not start with SEQ
      """
