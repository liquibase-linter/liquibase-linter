Feature: ChangeLog filename rule

    Scenario: Invalid changeLog filename
        When I run the liquibase linter in directory "changelog-file-name" with
            | configuration | changelog-file-name.json |
            | changelog     | file-name with-space.xml |
        Then A violation is detected with the following message
        """
        ChangeLog filename 'integration/changelog-file-name/file-name with-space.xml' must follow pattern '^[^ ]+$'
        """

    Scenario: Valid changeLog filename
        When I run the liquibase linter in directory "changelog-file-name" with
            | configuration | changelog-file-name.json      |
            | changelog     | file-name-no-spaces-valid.xml |
        Then No violation is detected
