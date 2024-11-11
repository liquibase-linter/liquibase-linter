---
title: primary-key-tablespace
---

## Why?

You might enforce that indexes related to primary keys are placed in a specific tablespace, distinct from the tablespace of tables.

This rule will fail if there is no `tablespace` given when creating a primary key, or when configured with a pattern, will fail if the given `tablespace` does not match the pattern.

## Options

- `pattern` - (regex, as string) optional regular expression that the `tablespace` of any created primary key must adhere to

## Example Usage

To simply ensure that a name is always given:

```json
{
    "rules": {
        "primary-key-tablespace": true
    }
}
```

To ensure that a pattern is matched:

```json
{
    "rules": {
        "primary-key-tablespace": {
            "pattern": "^IDX_[A-Z_]+$"
        }
    }
}
```
