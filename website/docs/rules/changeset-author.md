---
title: changeset-author
---

## Why?

You might want to enforce a naming pattern for your changeset author.

The `changeset-author` will fail if the given regex does not match against the `author` of the changeset.

## Options

- `pattern` - (regex, as string) regular expression that the `author` of any `changeset` must adhere to

## Example Usage

```json
{
  "rules": {
    "changeset-author": {
      "enabled": true,
      "pattern": "^(me|you)$"
    }
  }
}
```
