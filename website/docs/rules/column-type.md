---
title: column-type
---

## Why?

You might want to enforce a pattern for the type of your columns.

This rule will fail if the given regex does not match against the type when creating a column.

## Options

- `pattern` - (regex, as string) regular expression that the type of any created column must adhere to
- `columnCondition` - (string) - [Spring EL expression](https://www.baeldung.com/spring-expression-language) that should resolve to a boolean, which if provided will decide whether the rule should be applied or not to the evaluated column. The expression scope is [`ColumnConfig`](https://github.com/liquibase/liquibase/blob/master/liquibase-standard/src/main/java/liquibase/change/ColumnConfig.java) object

## Example Usage

```json
{
  "rules": {
    "column-type": {
      "pattern": "^int$",
      "columnCondition": "name == 'id'",
      "errorMessage": "Column type '%s' of column with name '%s' should be 'int'"
    }
  }
}
```

(The above example just ensures that columns named `id` should be of type `int`.)
