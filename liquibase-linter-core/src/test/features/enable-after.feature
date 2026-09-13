Feature: All changelog files before a specific changelog or changeset can be ignored with the "enable-after-changelog" or "enable-after-changeset" configuration

  Rule: "enable-after-changelog" configuration can be defined globally for all rules

    Scenario: Violations are ignored for the referenced changelog file and previous changelog files
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "enable-after-changelog": "no-comment-2.xml",
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

  Rule: "enable-after-changeset" ignores everything up to and including a specific changeset

    Scenario: Violations are ignored for the referenced changeset and every changeset before it
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "enable-after-changeset": { "changeLogFile": "changelog.xml", "id": "last-unlinted", "author": "legacy" },
          "rules": {
            "has-comment": true
          }
        }
        """
      And the main XML changelog file contains changesets
        """xml
        <changeSet id="first-unlinted" author="legacy">
          <insert tableName="FOO"><column name="BAR" value="v" /></insert>
        </changeSet>
        <changeSet id="last-unlinted" author="legacy">
          <insert tableName="FOO"><column name="BAR" value="v" /></insert>
        </changeSet>
        <changeSet id="first-linted" author="dev">
          <comment>a comment</comment>
          <insert tableName="FOO"><column name="BAR" value="v" /></insert>
        </changeSet>
        """
      When liquibase-linter runs
      Then no violation is detected

    Scenario: Violations are still reported for changesets parsed after the referenced changeset
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "enable-after-changeset": { "changeLogFile": "changelog.xml", "id": "last-unlinted", "author": "legacy" },
          "rules": {
            "has-comment": true
          }
        }
        """
      And the main XML changelog file contains changesets
        """xml
        <changeSet id="last-unlinted" author="legacy">
          <insert tableName="FOO"><column name="BAR" value="v" /></insert>
        </changeSet>
        <changeSet id="first-linted" author="dev">
          <insert tableName="FOO"><column name="BAR" value="v" /></insert>
        </changeSet>
        """
      When liquibase-linter runs
      Then a violation is detected with the following message
        """
        Change set must have a comment
        """

    Scenario: "enable-after-changeset" can also be defined per rule
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "rules": {
            "has-comment": {
              "enable-after-changeset": { "changeLogFile": "changelog.xml", "id": "last-unlinted", "author": "legacy" }
            }
          }
        }
        """
      And the main XML changelog file contains changesets
        """xml
        <changeSet id="last-unlinted" author="legacy">
          <insert tableName="FOO"><column name="BAR" value="v" /></insert>
        </changeSet>
        <changeSet id="first-linted" author="dev">
          <comment>a comment</comment>
          <insert tableName="FOO"><column name="BAR" value="v" /></insert>
        </changeSet>
        """
      When liquibase-linter runs
      Then no violation is detected

    Scenario: A per-rule "enable-after-changeset" still reports violations after the referenced changeset
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "rules": {
            "has-comment": {
              "enable-after-changeset": { "changeLogFile": "changelog.xml", "id": "last-unlinted", "author": "legacy" }
            }
          }
        }
        """
      And the main XML changelog file contains changesets
        """xml
        <changeSet id="last-unlinted" author="legacy">
          <insert tableName="FOO"><column name="BAR" value="v" /></insert>
        </changeSet>
        <changeSet id="first-linted" author="dev">
          <insert tableName="FOO"><column name="BAR" value="v" /></insert>
        </changeSet>
        """
      When liquibase-linter runs
      Then a violation is detected with the following message
        """
        Change set must have a comment
        """

  Rule: "enable-after-changelog" configuration can also be defined per rule

    Scenario: A rule accepts the camelCase alias "enableAfterChangelog"
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "rules": {
            "has-comment": {
              "enableAfterChangelog": "no-comment-2.xml"
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

    Scenario: A rule "enable-after-changelog" still reports violations after the referenced changelog
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "rules": {
            "has-comment": {
              "enable-after-changelog": "no-comment-1.xml"
            }
          }
        }
        """
      And the main XML changelog file contains changesets
        """xml
        <include relativeToChangelogFile="true" file="no-comment-1.xml" />
        <include relativeToChangelogFile="true" file="no-comment-2.xml" />
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
      When liquibase-linter runs
      Then a violation is detected with the following message
        """
        Change set must have a comment
        """
