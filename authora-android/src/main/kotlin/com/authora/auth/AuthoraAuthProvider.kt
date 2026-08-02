package com.authora.auth

interface AuthoraAuthProvider {
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun signUp(fullName: String, email: String, password: String): AuthResult
    suspend fun signOut()
    fun currentUser(): AuthoraUser?
}

data class AuthoraUser(
    val id: String,
    val email: String,
    val displayName: String?
)

sealed class AuthResult {
    data class Success(val user: AuthoraUser) : AuthResult()
    data class Failure(val message: String) : AuthResult()
    data class RequiresMfa(val challengeId: String) : AuthResult()
}