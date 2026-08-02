package com.authora.ui.signup

import com.authora.auth.MfaMethod

data class SignUpUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null,
    val isSignedUp: Boolean = false,
    val mfaChallengeId: String? = null,
    val mfaMethod: MfaMethod? = null,
    val mfaMaskedDestination: String? = null
)