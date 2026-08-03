# Providers

Authora ships with two built-in providers. Both implement the same `AuthoraAuthProvider` interface, so your UI code never needs to know which one is active.

```kotlin
interface AuthoraAuthProvider {
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun signUp(fullName: String, email: String, password: String): AuthResult
    suspend fun signOut()
    fun currentUser(): AuthoraUser?
    suspend fun verifyMfa(challengeId: String, code: String): AuthResult
    suspend fun resendMfaCode(challengeId: String): Boolean
}
```

# Firebase

```Bash
./gradlew authoraAddFirebase
```

Registration:

```Kotlin
AuthoraProviderRegistry.register("firebase") {
    FirebaseAuthoraProvider(FirebaseAuth.getInstance(), activity)
}
```

Supports SMS-based MFA out of the box via Firebase Multi-Factor Authentication.

# Supabase

```Bash
./gradlew authoraAddSupabase
```

Registration:

```Kotlin
AuthoraProviderRegistry.register("supabase") {
    val config = AuthoraConfigLoader.load(context).provider as ProviderConfig.Supabase
    val client = createSupabaseClient(config.url, config.anonKey) { install(Auth) }
    SupabaseAuthoraProvider(client)
}
```

Supports TOTP-based MFA via client.auth.mfa.

# Writing a Custom Provider

Implement AuthoraAuthProvider from authora-core and register it:

```Kotlin
class MyCustomProvider : AuthoraAuthProvider {
    override suspend fun signIn(email: String, password: String): AuthResult { ... }
    override suspend fun signUp(fullName: String, email: String, password: String): AuthResult { ... }
    override suspend fun signOut() { ... }
    override fun currentUser(): AuthoraUser? { ... }
    override suspend fun verifyMfa(challengeId: String, code: String): AuthResult { ... }
    override suspend fun resendMfaCode(challengeId: String): Boolean { ... }
}

AuthoraProviderRegistry.register("my_provider") { MyCustomProvider() }
```

Every screen in authora-android (Sign In, Sign Up, Account, MFA) works
with any provider that implements this interface — no UI
changes required.