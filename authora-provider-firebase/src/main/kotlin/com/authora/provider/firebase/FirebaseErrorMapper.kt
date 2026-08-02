package com.authora.provider.firebase

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

object FirebaseErrorMapper {
    fun toFriendlyMessage(throwable: Throwable): String = when (throwable) {
        is FirebaseAuthInvalidCredentialsException -> "The email or password you entered is incorrect."
        is FirebaseAuthInvalidUserException -> "No account was found with this email."
        is FirebaseAuthUserCollisionException -> "An account with this email already exists."
        is FirebaseAuthWeakPasswordException -> "This password is too weak. Please choose a stronger one."
        is FirebaseNetworkException -> "Network error. Please check your connection and try again."
        else -> throwable.message ?: "Something went wrong. Please try again."
    }
}