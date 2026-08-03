plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("plugin.serialization") version "2.0.21"
}

group = "io.github.authora-org"
version = "0.1.0"

android {
    namespace = "com.authora.provider.supabase"
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
    implementation(platform("io.github.jan-tennert.supabase:bom:3.0.3"))
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("io.ktor:ktor-client-okhttp:3.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
}