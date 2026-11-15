# liquibase-linter Gradle plugin

The `io.github.liquibase-linter` Gradle plugin. See the [documentation](https://liquibase-linter.github.io/liquibase-linter/docs/install#gradle) for its usage.

## How it is built

The plugin is built by **Gradle** (`build.gradle.kts`), with the standard tooling for Gradle plugins (`java-gradle-plugin`, `validatePlugins`, TestKit, `com.gradle.plugin-publish`). The `pom.xml` of this directory only runs that Gradle build from the Maven one, so that `mvn verify` at the root of the repository builds and tests everything:

1. `prepare-package`: `liquibase-linter-core`, and its parent POM, are copied from the Maven reactor to `target/staging-repository`, in the layout of a Maven repository.
2. `package`: `./gradlew build -PlinterRepository=target/staging-repository` is run. The Gradle build, and the test builds of its functional tests, resolve `liquibase-linter-core` from that repository, so they always use the core built by the same Maven build, never a stale one from `~/.m2`.

The version of the plugin is read from the root `pom.xml`. With `-DskipTests`, Gradle runs `assemble` instead of `build`. With `-Dgradle.skip=true`, the Gradle build is not run at all.

The plugin is not published to Maven Central (see `excludeArtifacts` in the root `pom.xml`), but to the Gradle Plugin Portal, by the release workflow, once `release:perform` has succeeded:

```shell
mvn exec:exec@gradle-publish-plugins
```

## Working on the plugin

From the root of the repository:

```shell
# Builds and tests everything, including the Gradle plugin
mvn verify

# Builds and tests the Gradle plugin, and the core it depends on, only
mvn verify -pl liquibase-linter-gradle-plugin -am
```

This directory is also a regular Gradle build. Run standalone, it resolves `liquibase-linter-core` from Maven local, so install it first:

```shell
mvn install -pl liquibase-linter-core -am -DskipTests
cd liquibase-linter-gradle-plugin
./gradlew build
./gradlew functionalTest --tests '*classpath*'
```

### IntelliJ IDEA

IntelliJ imports the repository as a Maven project, where this directory is the `liquibase-linter-gradle-plugin-build` module, without any source. To work on the plugin sources, also link this directory as a Gradle project: right-click on `build.gradle.kts`, then _Link Gradle Project_. Both projects can live side by side, as they have different names.
