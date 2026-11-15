import org.gradle.plugin.compatibility.compatibility

plugins {
    `java-gradle-plugin`
    alias(libs.plugins.plugin.publish)
}

group = "io.github.liquibase-linter"
// The Maven build is the single source of truth for the version, bumped by the maven-release-plugin
version = providers
    .fileContents(layout.projectDirectory.file("../pom.xml"))
    .asText
    .map { pom ->
        Regex("""<artifactId>liquibase-linter</artifactId>\s*<version>([^<]+)</version>""")
            .find(pom)
            ?.groupValues
            ?.get(1)
            ?: error("Cannot find the liquibase-linter version in ../pom.xml")
    }
    .get()

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
    withSourcesJar()
    withJavadocJar()
}

val linterCoreVersion = version.toString()

dependencies {
    // Not part of the plugin runtime classpath: the linter and Liquibase are loaded in an isolated worker classloader,
    // from the `liquibaseLinter` configuration, so that they never conflict with other plugins of the user build.
    compileOnly("io.github.liquibase-linter:liquibase-linter-core:$linterCoreVersion")
    compileOnly(libs.liquibase.core)
}

// Default versions of the tools, embedded in the plugin
val generatePluginProperties = tasks.register<WriteProperties>("generatePluginProperties") {
    destinationFile = layout.buildDirectory.file(
        "generated/plugin-properties/io/github/liquibaselinter/gradle/liquibase-linter-gradle-plugin.properties"
    )
    property("toolVersion", linterCoreVersion)
    property("liquibaseVersion", libs.versions.liquibase)
}
sourceSets.main {
    resources.srcDir(generatePluginProperties.map { layout.buildDirectory.dir("generated/plugin-properties") })
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.compilerArgs.addAll(listOf("-Xlint:all,-try", "-Werror", "-parameters"))
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).addBooleanOption("Xdoclint:all,-missing", true)
}

@Suppress("UnstableApiUsage")
testing {
    suites {
        val test = named<JvmTestSuite>("test") {
            useJUnitJupiter(libs.versions.junit)
            dependencies {
                // ProjectBuilder needs the Gradle API at runtime
                implementation(gradleApi())
                implementation(libs.assertj.core)
            }
        }

        register<JvmTestSuite>("functionalTest") {
            useJUnitJupiter(libs.versions.junit)
            dependencies {
                implementation(gradleTestKit())
                implementation(libs.assertj.core)
            }
            targets.configureEach {
                testTask.configure {
                    shouldRunAfter(test)
                    // Gradle versions the plugin is tested against, in addition to the one running this build:
                    // the minimum supported one (LiquibaseLinterPlugin.MINIMUM_GRADLE_VERSION), and the latest 8.x
                    systemProperty(
                        "liquibaseLinter.compatibilityGradleVersions",
                        providers.gradleProperty("compatibilityGradleVersions").getOrElse("8.8,8.14.7"),
                    )
                    // The test builds resolve liquibase-linter-core from the same repository as this build
                    val linterRepository = providers.gradleProperty("linterRepository")
                    if (linterRepository.isPresent) {
                        systemProperty("liquibaseLinter.repository", file(linterRepository.get()).toURI().toString())
                    }
                }
            }
        }
    }
}

tasks.check {
    dependsOn(testing.suites.named("functionalTest"))
}

tasks.validatePlugins {
    enableStricterValidation = true
    failOnWarning = true
}

gradlePlugin {
    website = "https://liquibase-linter.github.io/liquibase-linter/"
    vcsUrl = "https://github.com/liquibase-linter/liquibase-linter"
    testSourceSets(sourceSets["functionalTest"])
    plugins {
        register("liquibaseLinter") {
            id = "io.github.liquibase-linter"
            implementationClass = "io.github.liquibaselinter.gradle.LiquibaseLinterPlugin"
            displayName = "Liquibase Linter"
            description = "Check your Liquibase changelogs against configured lint rules"
            tags = listOf("liquibase", "lint", "linter", "database", "migration", "changelog")
            compatibility {
                features {
                    // Both checked by the functional tests
                    configurationCache = true
                    isolatedProjects = true
                }
            }
        }
    }
}
