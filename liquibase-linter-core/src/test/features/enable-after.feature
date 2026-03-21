Feature: All changelog files before a specific changelog can be ignored with the "enable-after" configuration

  Rule: "enable-after" configuration can be defined globally for all rules

    Scenario: Violations are ignored for changelog file referenced in "enable-after" config and previous changelog files
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "enable-after": "no-comment-2.xml",
          "rules": {
            "has-comment": true
          }
        }
        """
      And the main XML changelog file contains changesets
        """xml
        <include relativeToChangelogFile="true" file="no-comment-1.xml" />
        <include relativeToChangelogFile="true" file="no-comment-2.xml" />
        <include relativeToChangelogFile="true" file="with-comment.xml" />
        """
      And the additional XML changelog file "no-comment-1.xml" contains changes
        """xml
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
        """
      And the additional XML changelog file "no-comment-2.xml" contains changes
        """xml
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
        """
      And the additional XML changelog file "with-comment.xml" contains changes
        """xml
        <comment>some comment</comment>
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
        """
      When liquibase-linter runs
      Then no violation is detected

  Rule: "enable-after" configuration can also be defined per rule

    Scenario: Violations are ignored for changelog file referenced in "enable-after" in rule config and previous changelog files
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "enable-after": "no-comment-1.xml",
          "rules": {
            "has-comment": {
              "enableAfter": "no-comment-2.xml"
            }
          }
        }
        """
      And the main XML changelog file contains changesets
        """xml
        <include relativeToChangelogFile="true" file="no-comment-1.xml" />
        <include relativeToChangelogFile="true" file="no-comment-2.xml" />
        <include relativeToChangelogFile="true" file="with-comment.xml" />
        """
      And the additional XML changelog file "no-comment-1.xml" contains changes
        """xml
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
        """
      And the additional XML changelog file "no-comment-2.xml" contains changes
        """xml
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
        """
      And the additional XML changelog file "with-comment.xml" contains changes
        """xml
        <comment>some comment</comment>
        <insert tableName="FOO">
          <column name="BAR" value="test value" />
        </insert>
        """
      When liquibase-linter runs
      Then no violation is detected

    Scenario: Violations are ignored for changelog file referenced in "enable-after" in rule multi-config and previous changelog files
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "rules": {
            "object-name": [
              {
                "pattern": "^(?!_)[A-Z_0-9]+(?<!_)$",
                "errorMessage": "Object name '%s' name must be uppercase and use '_' separation",
                "enableAfter": "not-uppercase-object-name.xml"
              },
              {
                "pattern": "^POWER.*$",
                "errorMessage": "Object name '%s' name must begin with 'POWER'",
                "enableAfter": "uppercase-not-starting-with-power-object-name.xml"
              }
            ]
          }
        }
        """
      And the main XML changelog file contains changesets
        """xml
        <include relativeToChangelogFile="true" file="not-uppercase-object-name.xml" />
        <include relativeToChangelogFile="true" file="uppercase-not-starting-with-power-object-name.xml" />
        <include relativeToChangelogFile="true" file="valid-object-name.xml" />
        """
      And the additional XML changelog file "not-uppercase-object-name.xml" contains changes
        """xml
        <addColumn tableName="TEST">
          <column name="lower-case-object-name" type="NVARCHAR(00)" />
        </addColumn>
        """
      And the additional XML changelog file "uppercase-not-starting-with-power-object-name.xml" contains changes
        """xml
        <addColumn tableName="TEST">
          <column name="UPPER_CASE_BUT_NOT_STARTING_WITH_POWER" type="NVARCHAR(00)" />
        </addColumn>
        """
      And the additional XML changelog file "valid-object-name.xml" contains changes
        """xml
        <addColumn tableName="TEST">
          <column name="POWER_VALID_NAME" type="NVARCHAR(00)" />
        </addColumn>
        """
      When liquibase-linter runs
      Then no violation is detected
