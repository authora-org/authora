package com.authora.provider.firebase

import android.app.Activity
import com.authora.auth.AuthResult
import com.authora.auth.AuthoraAuthProvider
import com.authora.auth.AuthoraUser
import com.authora.auth.MfaMethod
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthMultiFactorException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.MultiFactorResolver
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.PhoneMultiFactorGenerator
import com.google.firebase.auth.PhoneMultiFactorInfo
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import java.util.UUID
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthoraProvider(
    private val firebaseAuth: FirebaseAuth,
    private val activity: Activity
) : AuthoraAuthProvider {

    private data class PendingMfa(
        val resolver: MultiFactorResolver,
        var verificationId: String? = null,
        var resendToken: PhoneAuthProvider.ForceResendingToken? = null
    )

    private val pendingChallenges = mutableMapOf<String, PendingMfa>()

    override suspend fun signIn(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: return AuthResult.Failure("Sign in failed. Please try again.")
            AuthResult.Success(user.toAuthoraUser())
        } catch (e: FirebaseAuthMultiFactorException) {
            startMfaChallenge(e.resolver)
        } catch (e: Exception) {
            AuthResult.Failure(FirebaseErrorMapper.toFriendlyMessage(e))
        }
    }

    override suspend fun signUp(fullName: String, email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user ?: return AuthResult.Failure("Sign up failed. Please try again.")

            val profileUpdate = UserProfileChangeRequest.Builder()
                .setDisplayName(fullName)
                .build()
            user.updateProfile(profileUpdate).await()

            AuthResult.Success(user.toAuthoraUser(displayNameOverride = fullName))
        } catch (e: Exception) {
            AuthResult.Failure(FirebaseErrorMapper.toFriendlyMessage(e))
        }
    }

    override suspend fun signOut() {
        firebaseAuth.signOut()
    }

    override fun currentUser(): AuthoraUser? {
        return firebaseAuth.currentUser?.toAuthoraUser()
    }

    override suspend fun verifyMfa(challengeId: String, code: String): AuthResult {
        val pending = pendingChallenges[challengeId]
            ?: return AuthResult.Failure("This verification session has expired. Please sign in again.")
        val verificationId = pending.verificationId
            ?: return AuthResult.Failure("Verification code was not sent yet. Please try resending it.")

        return try {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            val assertion = PhoneMultiFactorGenerator.getAssertion(credential)
            val result = pending.resolver.resolveSignIn(assertion).await()
            val user = result.user ?: return AuthResult.Failure("Verification failed. Please try again.")
            pendingChallenges.remove(challengeId)
            AuthResult.Success(user.toAuthoraUser())
        } catch (e: Exception) {
            AuthResult.Failure(FirebaseErrorMapper.toFriendlyMessage(e))
        }
    }

    override suspend fun resendMfaCode(challengeId: String): Boolean {
        val pending = pendingChallenges[challengeId] ?: return false
        val hint = pending.resolver.hints.firstOrNull { it is PhoneMultiFactorInfo } as? PhoneMultiFactorInfo
            ?: return false

        return try {
            sendVerificationCode(pending, hint, resend = true)
            true
        } catch (e: Exception) {
            false
        }
    }

    private suspend fun startMfaChallenge(resolver: MultiFactorResolver): AuthResult {
        val hint = resolver.hints.firstOrNull { it is PhoneMultiFactorInfo } as? PhoneMultiFactorInfo
            ?: return AuthResult.Failure("This account requires a verification method that is not supported yet.")

        val challengeId = UUID.randomUUID().toString()
        val pending = PendingMfa(resolver = resolver)
        pendingChallenges[challengeId] = pending

        return try {
            sendVerificationCode(pending, hint, resend = false)
            AuthResult.RequiresMfa(
                challengeId = challengeId,
                method = MfaMethod.SMS,
                maskedDestination = hint.phoneNumber
            )
        } catch (e: Exception) {
            pendingChallenges.remove(challengeId)
            AuthResult.Failure(FirebaseErrorMapper.toFriendlyMessage(e))
        }
    }

    private suspend fun sendVerificationCode(
        pending: PendingMfa,
        hint: PhoneMultiFactorInfo,
        resend: Boolean
    ) {
        suspendCancellableCoroutine<Unit> { continuation ->
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    if (continuation.isActive) continuation.resume(Unit)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    if (continuation.isActive) continuation.resumeWithException(e)
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    pending.verificationId = verificationId
                    pending.resendToken = token
                    if (continuation.isActive) continuation.resume(Unit)
                }
            }

            val optionsBuilder = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setMultiFactorSession(pending.resolver.session)
                .setMultiFactorHint(hint)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)

            if (resend) {
                pending.resendToken?.let { optionsBuilder.setForceResendingToken(it) }
            }

            PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
        }
    }

    private fun FirebaseUser.toAuthoraUser(displayNameOverride: String? = null): AuthoraUser {
        return AuthoraUser(
            id = uid,
            email = email ?: "",
            displayName = displayNameOverride ?: displayName
        )
    }
}