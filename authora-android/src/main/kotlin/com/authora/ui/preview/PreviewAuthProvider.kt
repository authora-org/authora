package com.authora.ui.preview

import com.authora.auth.AuthResult
import com.authora.auth.AuthoraAuthProvider
import com.authora.auth.AuthoraUser
import com.authora.auth.MfaMethod
import kotlinx.coroutines.delay

/**
 * Deterministic in-memory provider used only by AuthoraPreviewActivity.
 * No network calls are made. Use email "mfa@authora.dev" to preview the MFA flow,
 * and any password under 6 characters to preview a failed sign in.
 */
class PreviewAuthProvider : AuthoraAuthProvider {

    private var signedInUser: AuthoraUser? = null

    override suspend fun signIn(email: String, password: String): AuthResult {
        delay(600)
        if (email.equals("mfa@authora.dev", ignoreCase = true)) {
            return AuthResult.RequiresMfa(
                challengeId = "preview-challenge",
                method = MfaMethod.SMS,
                maskedDestination = "+62 8xx-xxx-x321"
            )
        }
        if (password.length < 6) {
            return AuthResult.Failure("The email or password you entered is incorrect.")
        }
        val user = AuthoraUser(id = "preview-user", email = email, displayName = "Preview User")
        signedInUser = user
        return AuthResult.Success(user)
    }

    override suspend fun signUp(fullName: String, email: String, password: String): AuthResult {
        delay(600)
        val user = AuthoraUser(id = "preview-user", email = email, displayName = fullName)
        signedInUser = user
        return AuthResult.Success(user)
    }

    override suspend fun signOut() {
        signedInUser = null
    }

    override fun currentUser(): AuthoraUser? = signedInUser

    override suspend fun verifyMfa(challengeId: String, code: String): AuthResult {
        delay(600)
        if (code != "123456") {
            return AuthResult.Failure("Verification code was incorrect. Please try again.")
        }
        val user = AuthoraUser(id = "preview-user", email = "mfa@authora.dev", displayName = "Preview User")
        signedInUser = user
        return AuthResult.Success(user)
    }

    override suspend fun resendMfaCode(challengeId: String): Boolean {
        delay(400)
        return true
    }
}