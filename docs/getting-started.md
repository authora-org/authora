# Getting Started

Authora is an opinionated authentication framework for Android. It gives you Sign In, Sign Up, Account management, MFA, and session handling out of the box — all driven by a single `Authora.toml` file.

## Philosophy

- One dependency.
- One configuration.
- One way.
- Build apps. Not authentication.

Authora does not give you dozens of ways to wire up auth. It gives you the right way.

## Quick Start

1. Apply the plugin in your app module (`app/build.gradle.kts`):

   ```kotlin
   plugins {
       id("com.authora.android") version "0.1.0"
   }
   ```

2. Connect a provider:

   ```Bash
   ./gradlew authoraAddFirebase
   # or
   ./gradlew authoraAddSupabase
   ```
   This creates and fills in Authora.toml at your app module's root,

3. Register the provider at app startup:

   ```Kotlin
   AuthoraProviderRegistry.register("firebase") {
       FirebaseAuthoraProvider(FirebaseAuth.getInstance(), this)
   }
   ```

4. Show the Sign In screen:

   ```Kotlin
   val authProvider = AuthoraProviderRegistry.create("firebase")
   val sessionStore = AuthoraSessionStore(context)

   SignInScreen(
       authProvider = authProvider,
       sessionStore = sessionStore,
       providerType = "firebase",
       onSignInSuccess = { /* navigate */ },
       onNavigateToSignUp = { /* navigate */ },
       onMfaRequired = { challengeId, method, masked -> /* navigate to MfaScreen */ }
   )
   ```

5. Run ./gradlew authoraPreview at any time to see all
   components live on a connected device, with no real network
   calls.

## Next Steps

[Installation](./installation.md) — module setup in detail
[Authora.toml](./authora-toml.md) — full configuration reference
[Providers](./providers.md) — Firebase, Supabase, and custom providers
[UI](./ui.md) — all available screens and components
[MFA](./mfa.md) — multi-factor authentication flow
[Custom Fields](./custom-fields.md) — extending Sign Up with your own fields