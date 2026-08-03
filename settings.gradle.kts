pluginManagement {
    includeBuild("authora-gradle-plugin")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "authora"

include(":authora-core")
include(":authora-android")
include(":authora-provider-firebase")
include(":authora-provider-supabase")