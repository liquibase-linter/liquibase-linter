---
title: sequence-name
---

## Why?

You might already have a broad standard for object names - and be enforcing it with [the object-name rule](object-name.md) - but you might also want a more specific rule concerning how sequences are named.

This rule will fail if the given regex does not match against the name when creating or renaming a sequence.

## Options

- `pattern` - (regex, as string) regular expression that the name of any created or renamed sequence must adhere to

## Example Usage

```json
{
  "rules": {
    "sequence-name": {
      "pattern": "^(?!seq).*$",
      "errorMessage": "Don't prefix sequence names with 'seq'"
    }
  }
}
```

(The above example just ensures that the `seq` prefix convention is not used.)
