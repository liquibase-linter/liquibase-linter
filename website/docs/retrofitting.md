---
title: Retrofitting
---

Using Liquibase Linter in a brand new project is pretty straightforward, but more often than not you'll be retrofitting it to an existing project with a history of changes. It's likely that many of those changes would not pass the set of rules you are applying, but since changes are supposed to be immutable, fixing them retrospectively is not really an option.

Liquibase Linter provides some extra configuration options to help with this.

## `enable-after-changelog` at project level

This config option allows you to specify a point in time (a change log file) _after_ which you want lint rules to be run. This would typically be the last change log before you add Liquibase Linter and turn on the rules.

Take this example configuration and change log:

```json
{
  "enable-after-changelog": "src/main/resources/example-1.xml",
  "rules": {}
}
```

```xml
<!-- root change log file -->
<databaseChangeLog
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
  xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.3.xsd"
>
  <include relativeToChangelogFile="true" file="example-1.xml" />
  <include relativeToChangelogFile="true" file="example-2.xml" />
  <include relativeToChangelogFile="true" file="example-3.xml" />
</databaseChangeLog>
```

Since we've called out `example-1.xml` as our `enable-after-changelog` change log, the linter will start checking from `example-2.xml`.

## `enable-after-changeset` at project level

When the boundary between "legacy" and "linted" changes does not line up with a change log file, you can point at a single changeset instead. It is referenced by its full Liquibase identity &mdash; the `changeLogFile`, `id` and `author` triple that uniquely identifies a changeset (the same three attributes as the [`changeSetExecuted`](https://docs.liquibase.com/concepts/changelogs/preconditions.html) precondition). **All three are mandatory**; `changeLogFile` is matched against the changeset's logical file path.

```json
{
  "enable-after-changeset": {
    "changeLogFile": "src/main/resources/example-2.xml",
    "id": "0002-add-customer-table",
    "author": "jsmith"
  },
  "rules": {}
}
```

The referenced changeset and every changeset before it are ignored; linting starts at the next changeset.

## `enable-after` (deprecated)

`enable-after` is the former name of `enable-after-changelog` and still behaves identically. It is kept for backward compatibility and will be removed in 1.0 &mdash; prefer `enable-after-changelog` in new configuration.

## At rule level

Over time you'll probably want to add new rules to your project &mdash; but again there may be historical changes that would fail if you just drop them in. The same two options are available per rule, with the same names:

```json
{
  "rules": {
    "has-context": {
      "enable-after-changelog": "last-changeset-before-contexts-became-mandatory.xml"
    },
    "has-comment": {
      "enable-after-changeset": {
        "changeLogFile": "src/main/resources/example-2.xml",
        "id": "0002-add-customer-table",
        "author": "jsmith"
      }
    }
  }
}
```

The camel-cased forms `enableAfterChangelog` and `enableAfterChangeset` are also accepted as aliases, at both levels. The legacy rule-level option is still spelled `enableAfter`.

## Only one boundary at a time

`enable-after`, `enable-after-changelog` and `enable-after-changeset` all express the same thing &mdash; the single point in history before which nothing is linted &mdash; so they are mutually exclusive. Setting more than one (at project level or within the same rule) fails configuration loading with an explicit error.
