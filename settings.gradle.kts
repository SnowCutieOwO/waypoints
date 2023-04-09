rootProject.name = "waypoints"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    plugins {
        val kotlinVersion: String by settings
        kotlin("jvm") version kotlinVersion

        val shadowVersion: String by settings
        id("com.github.johnrengelman.shadow") version shadowVersion

        val dokkaVersion: String by settings
        id("org.jetbrains.dokka") version dokkaVersion
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.4.0")
}


include(":waypoints")
include(":waypoints-api")
include(":api-base")
include(":api-sqlite")

include(":utils")
include(":pointers")