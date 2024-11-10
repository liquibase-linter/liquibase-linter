---
title: changeset-id
---

## Why?

You might want to enforce a naming pattern for the id of your changeset.

The `changeset-id` will fail if the given regex does not match against the `id` of the changeset.

## Options

- `pattern` - (regex, as string) regular expression that the `id` of any `changeset` must adhere to

## Example Usage

```json
{
    "rules": {
        "changeset-id": {
            "enabled": true,
            "pattern": "^\\d{8}_[a-z_]+$"
        }
    }
}
```
