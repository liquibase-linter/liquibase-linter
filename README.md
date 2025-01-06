# Liquibase Linter

_Quality control for your Liquibase migration scripts_

[![Build Status](https://github.com/liquibase-linter/liquibase-linter/workflows/build/badge.svg)](https://github.com/liquibase-linter/liquibase-linter/actions)
![Maven Central](https://img.shields.io/maven-central/v/io.github.liquibase-linter/liquibase-linter.svg)

## Description

_liquibase-linter_ is a tool to help you write better Liquibase migration scripts.
It checks your scripts against a set of rules to ensure they are well-formed and maintainable.

This is a friendly fork of the original [liquibase-linter](https://github.com/whiteclarkegroup/liquibase-linter) project that is unmaintained since 2021.

## Get Started

### With the liquibase-maven-plugin

If you're already using the `liquibase-maven-plugin`, you can add `liquibase-parser-extension` that will lint automatically your scripts when they're parsed by Liquibase:

**1** Add `liquibase-parser-extension` to your pom as a dependency of `liquibase-maven-plugin`:

```xml
<plugin>
  <groupId>org.liquibase</groupId>
  <artifactId>liquibase-maven-plugin</artifactId>
  <configuration>...</configuration>
  <dependencies>
    <dependency>
      <groupId>io.github.liquibase-linter</groupId>
      <artifactId>liquibase-parser-extension</artifactId>
      <version>0.9.1</version>
    </dependency>
  </dependencies>
  <executions>...</executions>
</plugin>
```

**2** Add the [config file](https://liquibase-linter.github.io/liquibase-linter/docs/configure) to your project root, and start turning [on rules](https://liquibase-linter.github.io/liquibase-linter/docs/rules/).

### With the dedicated liquibase-linter-maven-plugin

**1** Add the `liquibase-linter-maven-plugin` to your pom:

```xml
<plugin>
  <groupId>io.github.liquibase-linter</groupId>
  <artifactId>liquibase-linter-maven-plugin</artifactId>
  <version>0.9.1</version>
  <configuration>
    <changeLogFile>src/main/resources/config/liquibase/master.xml</changeLogFile>
    <!-- This is the default value for configurationFile -->
    <configurationFile>src/test/resources/lqlint.json</configurationFile>
  </configuration>
  <executions>
    <execution>
      <id>lint-liquibase-scripts</id>
      <goals>
        <goal>lint</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

**2** Add the [config file](https://liquibase-linter.github.io/liquibase-linter/docs/configure), and start turning [on rules](https://liquibase-linter.github.io/liquibase-linter/docs/rules/).

Try the [full documentation](https://liquibase-linter.github.io/liquibase-linter/docs/install) for details of config and rules.
