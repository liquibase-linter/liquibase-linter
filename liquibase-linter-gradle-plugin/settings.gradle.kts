plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "liquibase-linter-gradle-plugin"

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        mavenCentral()
        // liquibase-linter-core is built by the Maven build of this repository, which stages it in `linterRepository`
        // when it runs this build. Run standalone, this build resolves it from Maven local: run `mvn install` first.
        val linterRepository = providers.gradleProperty("linterRepository")
        if (linterRepository.isPresent) {
            maven {
                name = "linterStaging"
                url = file(linterRepository.get()).toURI()
                content {
                    includeGroup("io.github.liquibase-linter")
                }
            }
        } else {
            mavenLocal {
                content {
                    includeGroup("io.github.liquibase-linter")
                }
            }
        }
    }
}
