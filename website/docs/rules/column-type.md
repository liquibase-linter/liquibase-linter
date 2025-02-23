---
title: column-type
---

## Why?

You might want to enforce a pattern for the type of your columns.

This rule will fail if the given regex does not match against the type when creating a column.

## Options

- `pattern` - (regex, as string) regular expression that the type of any created column must adhere to

## Example Usage

```json
{
  "rules": {
    "column-type": {
      "pattern": "^[a-z_]+$",
      "errorMessage": "Column type '%s' of column '%s' should be lower cased"
    }
  }
}
```

(The above example just ensures that columns should have a lowercased type.)
