# Installation

Authora is split into small modules so your app only pulls in what it uses.

| Module | Purpose |
|---|---|
| `com.authora.android:authora-gradle-plugin` | Gradle plugin — reads `Authora.toml`, wires dependencies, provides `authoraAddFirebase`, `authoraAddSupabase`, `authoraPreview`, `authoraUpgrade` |
| `io.github.authora-org:authora-core` | Runtime config model, `AuthoraAuthProvider` interface, session storage |
| `io.github.authora-org:authora-android` | Compose UI (Sign In, Sign Up, Account, MFA, etc.) |
| `io.github.authora-org:authora-provider-firebase` | Firebase Authentication implementation |
| `io.github.authora-org:authora-provider-supabase` | Supabase Auth implementation |

## 1. Add the plugin repository

While Authora is not yet published, publish it to your local Maven repository from source:

```bash
./gradlew publishToMavenLocal
```

In settings.gradle.kts:
```Kotlin
pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}
```

## 2. Apply the plugin

app/build.gradle.kts:

```Kotlin
plugins {
    id("com.android.application")
    kotlin("android")
    id("io.github.authora-org.android") version "0.1.0"
}

android {
    defaultConfig {
        minSdk = 24
    }
}
```

authora-android and authora-core require minSdk = 24 or higher.

## 3. Bootstrap Authora.toml

Run either:

```Bash
./gradlew authoraAddFirebase
#or
./gradlew authoraAddSupabase
```

Both create Authora.toml at the app module root if it doesn't exist
yet, and keep it updated on every run.

## 4. Sync assets

The plugin automatically copies Authora.toml into src/main/assets/Authora.toml
before every build (via the authoraSyncToml task, hooked into preBuild),
so AuthoraConfigLoader can read it at runtime.