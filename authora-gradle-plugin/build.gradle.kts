plugins {
    `java-gradle-plugin`
    kotlin("jvm") version "2.0.21"
    id("com.gradle.plugin-publish") version "1.3.0"
}

fun resolveGitVersion(): String {
    return try {
        val process = ProcessBuilder("git", "describe", "--tags", "--exact-match")
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readText().trim()
        process.waitFor()
        if (process.exitValue() == 0 && output.startsWith("v")) {
            output.removePrefix("v")
        } else {
            "0.0.0-SNAPSHOT"
        }
    } catch (e: Exception) {
        "0.0.0-SNAPSHOT"
    }
}

group = "io.github.authora-org"
version = resolveGitVersion()

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.google.gms:google-services:4.4.2")
    implementation("org.json:json:20240303")
}

gradlePlugin {
    website.set("https://github.com/authora-org/authora")
    vcsUrl.set("https://github.com/authora-org/authora.git")

    plugins {
        create("authoraAndroid") {
            id = "io.github.authora-org.android"
            implementationClass = "com.authora.gradle.AuthoraPlugin"
            displayName = "Authora Android Plugin"
            description = "Opinionated authentication framework plugin for Android"
            tags.set(listOf("android", "authentication", "firebase", "supabase"))
        }
    }
}

kotlin {
    jvmToolchain(17)
}