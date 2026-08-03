plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.serialization") version "2.0.21"
}

group = "io.github.authora-org"
version = "0.1.0"

android {
    namespace = "com.authora.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}