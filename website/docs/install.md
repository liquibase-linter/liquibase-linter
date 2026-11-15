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
  <version>1.1.0</version>
  <configuration>
    <changeLogFile>src/main/resources/config/liquibase/master.xml</changeLogFile>
    <!-- This is the default value for configurationFile -->
    <configurationFile>src/test/resources/lqlint.json</configurationFile>
  </configuration>
  <executions>
    <execution>
      <id>lint-liquibase-scripts</id>
      <goals>
        <goal>check</goal>
      </goals>
    </execution>
  </executions>
</plugin>
```

To skip the check, set the `skip` parameter to `true` in the plugin configuration, or pass the `liquibase-linter.skip` property on the command line:

```shell
mvn verify -Dliquibase-linter.skip=true
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
      <version>1.1.0</version>
    </dependency>
  </dependencies>
  <executions>...</executions>
</plugin>
```

## Gradle

### Using the Gradle plugin

If you just want to lint your liquibase scripts during build, use the Gradle plugin provided by Liquibase Linter. It requires Gradle 8.8 or later.

1. Apply the `io.github.liquibase-linter` plugin to your project.
2. Add `lqlint.json` to the `src/test/resources` directory of your project. This is the default location, but you can change it in the plugin configuration.

See this simple [example](https://github.com/liquibase-linter/liquibase-linter/tree/main/examples/liquibase-linter-gradle-plugin) gradle project to help get you started.

```kotlin
plugins {
    id("io.github.liquibase-linter") version "1.1.0"
}

repositories {
    mavenCentral()
}

liquibaseLinter {
    changeLogFile = "src/main/resources/db/changelog/db.changelog-master.xml"
    // This is the default value for configurationFile
    configurationFile = file("src/test/resources/lqlint.json")
}
```

The plugin registers a `liquibaseLinterCheck` task, which is run by the `check` task. The task is compatible with the configuration cache and the build cache, and is up-to-date as long as neither the changelogs nor the configuration change. A text report is written to `build/reports/liquibase-linter/liquibaseLinterCheck.txt`.

The linter, and the Liquibase version it uses to parse the changelogs, are resolved from the repositories of your project, in an isolated classloader, so they never conflict with the other plugins of your build.

All the settings of the `liquibaseLinter` extension:

| Setting             | Default                          | Description                                                                                                                                       |
| ------------------- | -------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------- |
| `changeLogFile`     | _required_                       | The root changelog: a path relative to the project directory, or a resource of the main runtime classpath, optionally prefixed with `classpath:`. |
| `configurationFile` | `src/test/resources/lqlint.json` | The liquibase-linter configuration file (`lqlint.json` or `lqlint.jsonc`).                                                                        |
| `ignoreFailures`    | `false`                          | Whether the build should go on when violations are found.                                                                                         |
| `skip`              | `false`                          | Skips the check. Can also be set with the `liquibase-linter.skip` Gradle property.                                                                |
| `liquibaseVersion`  | `4.33.0`                         | The version of Liquibase used to parse the changelogs.                                                                                            |
| `toolVersion`       | the version of the plugin        | The version of liquibase-linter used to lint the changelogs.                                                                                      |

To skip the check, set `skip` to `true`, or pass the `liquibase-linter.skip` property on the command line:

```shell
./gradlew build -Pliquibase-linter.skip=true
```

[Custom rules](custom-rules.md) are added to the `liquibaseLinter` configuration:

```kotlin
dependencies {
    liquibaseLinter("com.example:my-liquibase-linter-rules:1.0.0")
}
```

To lint several root changelogs, register more tasks of type `LiquibaseLinterCheck`:

```kotlin
import io.github.liquibaselinter.gradle.LiquibaseLinterCheck

val checkLegacyChangeLog = tasks.register<LiquibaseLinterCheck>("checkLegacyChangeLog") {
    changeLogFile = "src/main/resources/db/legacy/master.xml"
}
tasks.check {
    dependsOn(checkLegacyChangeLog)
}
```

### Using the Liquibase Gradle plugin

If you're already using [the Liquibase Gradle plugin](https://github.com/liquibase/liquibase-gradle-plugin), you can add `liquibase-parser-extension` that will lint automatically your scripts when they're parsed by Liquibase:

1. Add `liquibase-parser-extension` as a dependency of [the Liquibase Gradle plugin](https://github.com/liquibase/liquibase-gradle-plugin):
2. Add `lqlint.json` to the `lqlint` directory under the root of your project

See this simple [example](https://github.com/liquibase-linter/liquibase-linter/tree/main/examples/gradle) gradle project to help get you started

```groovy
dependencies {
    liquibaseRuntime 'org.liquibase:liquibase-core:4.29.2'
    liquibaseRuntime 'org.liquibase:liquibase-groovy-dsl:4.0.0'
    liquibaseRuntime 'org.hsqldb:hsqldb:2.5.0'
    liquibaseRuntime 'io.github.liquibase-linter:liquibase-parser-extension:1.1.0'
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

As for Java support, Liquibase Linter needs at least Java 17, but you should have no issues with higher versions, unless they are with Liquibase itself.
