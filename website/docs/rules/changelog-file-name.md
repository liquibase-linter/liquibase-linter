---
title: changelog-file-name
---

## Why?

You might want to enforce a naming pattern for your changelog files.

The `changelog-file-name` rule matches the regex you provide against any `databaseChangeLog` filename, and fails where it is not matched.

## Options

- `pattern` - (regex, as string) regular expression that any `databaseChangeLog` filename should adhere to

## Example Usage

```json
{
  "rules": {
    "changelog-file-name": {
      "enabled": true,
      "pattern": "^\\d{8}_[a-z_]+$"
    }
  }
}
```
