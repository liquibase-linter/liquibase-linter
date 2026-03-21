Feature: Rule "file-not-included"

  Rule: The "file-not-included" rule will fail if any file in the specified target directories were not included

    Background:
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "rules": {
            "file-not-included": {
              "values": ["to-include/"]
            }
          }
        }
        """
      And the additional XML changelog file "to-include/changelog-1.xml" contains changesets
        """xml
        <changeSet id="changeset-1" author="liquibase-linter-tests" />
        """
      And the additional XML changelog file "to-include/changelog-2.xml" contains changesets
        """xml
        <changeSet id="changeset-2" author="liquibase-linter-tests" />
        """

    Scenario: Should not allow file not included in deltas change log
      Given the main XML changelog file contains changesets
        """xml
        <include relativeToChangelogFile="true" file="to-include/changelog-1.xml" />
        """
      When liquibase-linter runs
      Then a violation is detected with the following message
        """
        Changelog files not included in deltas change log: to-include/changelog-2.xml
        """

    Scenario: Should allow all files included in deltas change log
      Given the main XML changelog file contains changesets
        """xml
        <include relativeToChangelogFile="true" file="to-include/changelog-1.xml" />
        <include relativeToChangelogFile="true" file="to-include/changelog-2.xml" />
        """
      When liquibase-linter runs
      Then no violation is detected
