Feature: The Liquibase Linter configuration can import configuration from other configuration files

  Rule: If multiple configurations are imported, their rules are combined and all violations are reported

    Background:
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "import": ["lqlint-chars.json", "lqlint-prefix.json"]
        }
        """
      And an additional configuration file "lqlint-chars.json" has content
        """json
        {
          "rules": {
            "object-name": {
              "pattern": "^[A-Z_]+$",
              "errorMessage": "Object name '%s' name must be uppercase letters and use '_' separation"
            }
          }
        }
        """
      And an additional configuration file "lqlint-prefix.json" has content
        """json
        {
          "rules": {
            "object-name": {
              "pattern": "^OBJ_.*$",
              "errorMessage": "Object name '%s' name must start with 'OBJ_'"
            }
          }
        }
        """

    Scenario: Violation of first imported rule is detected
      Given the main XML changelog file contains changes
        """xml
        <addColumn tableName="TEST">
          <column name="OBJ_COLUMN1" type="NVARCHAR(00)" />
        </addColumn>
        """
      When liquibase-linter runs
      Then a violation is detected with the following message
        """
        Object name 'OBJ_COLUMN1' name must be uppercase letters and use '_' separation
        """

    Scenario: Violation of second imported rule is also detected
      Given the main XML changelog file contains changes
        """xml
        <addColumn tableName="TEST">
          <column name="COLUMN" type="NVARCHAR(00)" />
        </addColumn>
        """
      When liquibase-linter runs
      Then a violation is detected with the following message
        """
        Object name 'COLUMN' name must start with 'OBJ_'
        """

    Scenario: No violation is detected when both imported rules are satisfied
      Given the main XML changelog file contains changes
        """xml
        <addColumn tableName="TEST">
          <column name="OBJ_COLUMN" type="NVARCHAR(00)" />
        </addColumn>
        """
      When liquibase-linter runs
      Then no violation is detected

  Rule: Imported configuration can also import other configuration files, and all rules from the imported configurations will be applied

    Background:
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "import": ["lqlint-nested.json"]
        }
        """
      And an additional configuration file "lqlint-nested.json" has content
        """json
        {
          "import": ["lqlint-chars.json"]
        }
        """
      And an additional configuration file "lqlint-chars.json" has content
        """json
        {
          "rules": {
            "object-name": {
              "pattern": "^[A-Z_]+$",
              "errorMessage": "Object name '%s' name must be uppercase letters and use '_' separation"
            }
          }
        }
        """

    Scenario: Violation of rule from nested imported configuration is detected
      Given the main XML changelog file contains changes
        """xml
        <addColumn tableName="TEST">
          <column name="LETTERS_AND_DIGIT_1" type="NVARCHAR(00)" />
        </addColumn>
        """
      When liquibase-linter runs
      Then a violation is detected with the following message
        """
        Object name 'LETTERS_AND_DIGIT_1' name must be uppercase letters and use '_' separation
        """

  Rule: Any named rules in the main configuration will replace all rules of the same name from the imported configuration, thus being overridden.

    Background:
      Given liquibase-linter is configured with
        """json
        {
          "fail-fast": true,
          "import": "lqlint-imported.json",
          "rules": {
            "object-name": {
              "pattern": "^[A-Z0-9]+$",
              "errorMessage": "Object name '%s' name must be uppercase or numeric characters only"
            }
          }
        }
        """
      And an additional configuration file "lqlint-imported.json" has content
        """json
        {
          "rules": {
            "object-name": {
              "pattern": "^[A-Z_]+$",
              "errorMessage": "Object name '%s' name must be uppercase letters and use '_' separation"
            }
          }
        }
        """

    Scenario: Rule override in main configuration is applied instead of imported rule
      Given the main XML changelog file contains changes
        """xml
        <addColumn tableName="TEST">
          <column name="OBJ_COLUMN" type="NVARCHAR(00)" />
        </addColumn>
        """
      When liquibase-linter runs
      Then a violation is detected with the following message
        """
        Object name 'OBJ_COLUMN' name must be uppercase or numeric characters only
        """

    Scenario: Overridden rule is not applied when object name does not violate the override rule
      Given the main XML changelog file contains changes
        """xml
        <addColumn tableName="TEST">
          <column name="COLUMN1" type="NVARCHAR(00)" />
        </addColumn>
        """
      When liquibase-linter runs
      Then no violation is detected
