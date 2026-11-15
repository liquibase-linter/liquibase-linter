plugins {
    java
    id("io.github.liquibase-linter") version "1.1.0"
}

repositories {
    mavenCentral()
}

liquibaseLinter {
    changeLogFile = "src/main/resources/config/liquibase/master.xml"
    // This is the default value for configurationFile
    configurationFile = file("src/test/resources/lqlint.json")
}
