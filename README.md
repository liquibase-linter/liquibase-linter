# Liquibase Linter

*Quality control for your Liquibase migration scripts*

[![Build Status](https://github.com/liquibase-linter/liquibase-linter/workflows/build/badge.svg)](https://github.com/liquibase-linter/liquibase-linter/actions)
![Maven Central](https://img.shields.io/maven-central/v/io.github.liquibase-linter/liquibase-linter.svg) 

## Description

*liquibase-linter* is a tool to help you write better Liquibase migration scripts. 
It checks your scripts against a set of rules to ensure they are well-formed and maintainable.

This is a friendly fork of the original [liquibase-linter](https://github.com/whiteclarkegroup/liquibase-linter) project that is unmaintained since 2021.

## Get Started

**1** Add `liquibase-linter` to your pom as a dependency of `liquibase-maven-plugin`:

```xml
<plugin>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-maven-plugin</artifactId>
    <configuration>
        ...
    </configuration>
    <dependencies>
        <dependency>
            <groupId>io.github.liquibase-linter</groupId>
            <artifactId>liquibase-parser-extension</artifactId>
            <version>0.6.0</version>
        </dependency>
    </dependencies>
    <executions>
        ...
    </executions>
</plugin>

```

**2** Add the [config file](https://liquibase-linter.github.io/liquibase-linter/docs/configure) to your project root, and start turning [on rules](https://liquibase-linter.github.io/liquibase-linter/docs/rules/).

Try the [full documentation](https://liquibase-linter.github.io/liquibase-linter/docs/install) for details of config and rules.
