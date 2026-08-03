plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.serialization") version "2.0.21"
}

group = "io.github.authora-org"
version = "0.1.0"

android {
    namespace = "com.authora.provider.firebase"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":authora-core"))
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.9.0")
}