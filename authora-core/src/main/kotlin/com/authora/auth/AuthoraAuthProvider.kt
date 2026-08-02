package com.authora.auth

interface AuthoraAuthProvider {
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun signUp(fullName: String, email: String, password: String): AuthResult
    suspend fun signOut()
    fun currentUser(): AuthoraUser?
    suspend fun verifyMfa(challengeId: String, code: String): AuthResult
    suspend fun resendMfaCode(challengeId: String): Boolean
}

data class AuthoraUser(
    val id: String,
    val email: String,
    val displayName: String?
)

enum class MfaMethod {
    SMS,
    TOTP,
    EMAIL
}

sealed class AuthResult {
    data class Success(val user: AuthoraUser) : AuthResult()
    data class Failure(val message: String) : AuthResult()
    data class RequiresMfa(
        val challengeId: String,
        val method: MfaMethod,
        val maskedDestination: String? = null
    ) : AuthResult()
}