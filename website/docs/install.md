---
title: Install
---

There are a few ways to set up Liquibase Linter, depending on how you're using Liquibase.

## Maven

### Using the liquibase-linter-maven-plugin

If you just want to lint your liquibase scripts during build, but are not already using the `liquibase-maven-plugin`, you probably want to use the maven plugin provided by Liquibase Linter:

1. Add the `liquibase-linter-maven-plugin` to your pom.
2. Add `lqlint.json` to the `src/test/resources` directory of your project. This is the default location, but you can change it in the plugin configuration.

See this simple [example](https://github.com/liquibase-linter/liquibase-linter/tree/main/examples/liquibase-linter-maven-plugin) maven project to help get you started.

```xml
<plugin>
  <groupId>io.github.liquibase-linter</groupId>
  <artifactId>liquibase-linter-maven-plugin</artifactId>
  <version>0.11.0</version>
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

### Using the liquibase-maven-plugin

If you're already using the `liquibase-maven-plugin`, you can add `liquibase-parser-extension` that will lint automatically your scripts when they're parsed by Liquibase, using [the Extensions feature in Liquibase](https://contribute.liquibase.com/extensions-integrations/extensions-overview/):

1. Add `liquibase-parser-extension` as a dependency of [the Liquibase Maven plugin](https://docs.liquibase.com/tools-integrations/maven/home.html):
2. Add `lqlint.json` to the root of your project

See this simple [example](https://github.com/liquibase-linter/liquibase-linter/tree/main/examples/liquibase-maven-plugin) maven project to help get you started

```xml
<plugin>
  <groupId>org.liquibase</groupId>
  <artifactId>liquibase-maven-plugin</artifactId>
  <configuration>...</configuration>
  <dependencies>
    <dependency>
      <groupId>io.github.liquibase-linter</groupId>
      <artifactId>liquibase-parser-extension</artifactId>
      <version>0.11.0</version>
    </dependency>
  </dependencies>
  <executions>...</executions>
</plugin>
```

## Gradle

1. Add `liquibase-parser-extension` as a dependency of [the Liquibase Gradle plugin](https://github.com/liquibase/liquibase-gradle-plugin):
2. Add `lqlint.json` to the `lqlint` directory under the root of your project

See this simple [example](https://github.com/liquibase-linter/liquibase-linter/tree/main/examples/gradle) gradle project to help get you started

```groovy
dependencies {
    liquibaseRuntime 'org.liquibase:liquibase-core:4.29.2'
    liquibaseRuntime 'org.liquibase:liquibase-groovy-dsl:4.0.0'
    liquibaseRuntime 'org.hsqldb:hsqldb:2.5.0'
    liquibaseRuntime 'io.github.liquibase-linter:liquibase-parser-extension:0.11.0'
    liquibaseRuntime files('lqlint')
}
```

## Command Line

1. Start with the latest [Liquibase release zip](https://github.com/liquibase/liquibase/releases/).
2. Download the latest Liquibase Linter jar from [maven central](https://repo1.maven.org/maven2/io/github/liquibase-linter/) and download
   the [dependencies](https://mvnrepository.com/artifact/io.github.liquibase-linter/liquibase-linter) required by Liquibase Linter, then add them to
   the `lib` directory.
3. Add your `lqlint.json` configuration file to the `lib` directory.

## Compatibility

It doesn't matter whether you use Liquibase scripts written in XML, JSON or YAML, they will be linted just the same.

Liquibase Linter has been tested with Liquibase versions 4.0 through to the latest version, so you can confidently use it with those. We'll be working to keep up with newer versions of Liquibase as they happen.

As for Java support, Liquibase Linter needs at least Java 8, but you should have no issues with higher versions, unless they are with Liquibase itself.
