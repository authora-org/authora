package com.authora.provider.supabase

import com.authora.auth.AuthResult
import com.authora.auth.AuthoraAuthProvider
import com.authora.auth.AuthoraUser
import com.authora.auth.MfaMethod
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class SupabaseAuthoraProvider(
    private val client: SupabaseClient
) : AuthoraAuthProvider {

    override suspend fun signIn(email: String, password: String): AuthResult {
        return try {
            client.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            val factorId = client.auth.mfa.verifiedFactors
                .firstOrNull { it.factorType == "totp" }
                ?.id

            if (factorId != null) {
                val challenge = client.auth.mfa.createChallenge(factorId)
                return AuthResult.RequiresMfa(
                    challengeId = "$factorId:${challenge.id}",
                    method = MfaMethod.TOTP
                )
            }

            val user = client.auth.currentUserOrNull()
                ?: return AuthResult.Failure("Sign in failed. Please try again.")
            AuthResult.Success(user.toAuthoraUser())
        } catch (e: Exception) {
            AuthResult.Failure(SupabaseErrorMapper.toFriendlyMessage(e))
        }
    }

    override suspend fun signUp(fullName: String, email: String, password: String): AuthResult {
        return try {
            client.auth.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            client.auth.updateUser {
                data = buildJsonObject {
                    put("full_name", fullName)
                }
            }
            val user = client.auth.currentUserOrNull()
                ?: return AuthResult.Failure("Sign up failed. Please try again.")
            AuthResult.Success(user.toAuthoraUser(displayNameOverride = fullName))
        } catch (e: Exception) {
            AuthResult.Failure(SupabaseErrorMapper.toFriendlyMessage(e))
        }
    }

    override suspend fun signOut() {
        client.auth.signOut()
    }

    override fun currentUser(): AuthoraUser? {
        return client.auth.currentUserOrNull()?.toAuthoraUser()
    }

    override suspend fun verifyMfa(challengeId: String, code: String): AuthResult {
        val parts = challengeId.split(":")
        if (parts.size != 2) return AuthResult.Failure("This verification session has expired. Please sign in again.")
        val (factorId, rawChallengeId) = parts

        return try {
            client.auth.mfa.verifyChallenge(factorId = factorId, challengeId = rawChallengeId, code = code)
            val user = client.auth.currentUserOrNull()
                ?: return AuthResult.Failure("Verification failed. Please try again.")
            AuthResult.Success(user.toAuthoraUser())
        } catch (e: Exception) {
            AuthResult.Failure(SupabaseErrorMapper.toFriendlyMessage(e))
        }
    }

    override suspend fun resendMfaCode(challengeId: String): Boolean {
        val factorId = challengeId.substringBefore(":")
        return try {
            client.auth.mfa.createChallenge(factorId)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun UserInfo.toAuthoraUser(displayNameOverride: String? = null): AuthoraUser {
        val fullName = displayNameOverride ?: userMetadata?.get("full_name")?.toString()?.trim('"')
        return AuthoraUser(id = id, email = email ?: "", displayName = fullName)
    }
}