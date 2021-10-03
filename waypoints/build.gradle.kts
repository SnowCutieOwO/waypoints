import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

group = "de.md5lukas"
version = "3.0.0-SNAPSHOT"
description = "Waypoints plugin"

dependencies {
    implementation("org.spigotmc:spigot-api:1.14.4-R0.1-SNAPSHOT")
    implementation(kotlin("stdlib-jdk8"))

    implementation(project(":waypoints-api"))

    implementation("com.github.MilkBowl:VaultAPI:1.7.1")

    implementation("de.md5lukas:md5-commons:2.0.0-SNAPSHOT")
    implementation("de.md5lukas:sqlite-kotlin-helper:1.0.1")
    implementation("de.md5lukas:kinvs:1.0.0-SNAPSHOT")

    implementation("net.wesjd:anvilgui:1.5.3-SNAPSHOT")

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.2")
}

tasks.withType<ProcessResources> {
    // Force refresh, because gradle does not detect changes in the variables used by expand
    this.outputs.upToDateWhen { false }

    filesMatching("**/plugin.yml") {
        expand("version" to project.version)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("")

    dependencies {
        include(dependency("org.jetbrains.kotlin::"))

        include(project(":waypoints-api"))

        include(dependency("de.md5lukas:md5-commons"))
        include(dependency("de.md5lukas:sqlite-kotlin-helper"))
        include(dependency("de.md5lukas:kinvs"))

        include(dependency("net.wesjd:anvilgui"))
    }

    relocate("kotlin", "de.md5lukas.waypoints.kt")

    relocate("de.md5lukas.commons", "de.md5lukas.waypoints.commons")
    relocate("de.md5lukas.jdbc", "de.md5lukas.waypoints.jdbc")
    relocate("de.md5lukas.kinvs", "de.md5lukas.waypoints.kinvs")

    relocate("net.wesjd.anvilgui", "de.md5lukas.waypoints.anvilgui")
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        events("passed", "skipped", "failed")
    }
}
