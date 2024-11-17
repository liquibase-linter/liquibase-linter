---
title: column-name
---

## Why?

You might already have a broad standard for object names - and be enforcing it with [the object-name rule](object-name.md) - but you might also want a more specific rule concerning how columns are named.

This rule will fail if the given regex does not match against the name when creating or renaming a column.

## Options

- `pattern` - (regex, as string) regular expression that the name of any created or renamed column must adhere to

## Example Usage

```json
{
  "rules": {
    "column-name": {
      "pattern": "^[a-z_]+$",
      "errorMessage": "Column name '%s' should be lower cased"
    }
  }
}
```

(The above example just ensures that columns should not be named `id`.)
