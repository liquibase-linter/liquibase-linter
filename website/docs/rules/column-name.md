---
title: column-name
---

## Why?

You might already have a broad standard for object names - and be enforcing it with [the object-name rule](object-name.md) - but you might also want a more specific rule concerning how columns are named.

This rule will fail if the given regex does not match against the name when creating or renaming a column.

## Options

- `pattern` - (regex, as string) regular expression that the name of any created or renamed column must adhere to
- `invertMatch` - (boolean) if true, the regex will be inverted, so that the rule will fail if the name matches the pattern

## Example Usage

```json
{
    "rules": {
        "column-name": {
            "pattern": "^col_[a-z_]+"
        }
    }
}
``` 

```json
{
    "rules": {
        "column-name": {
            "pattern": "^id$",
            "invertMatch": true,
            "errorMessage": "Column name 'id' is forbidden"
        }
    }
}
``` 

(The above example just ensures that columns should not be named `id`.)
