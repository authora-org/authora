package com.authora.ui.signin

import com.authora.auth.MfaMethod

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null,
    val isSignedIn: Boolean = false,
    val mfaChallengeId: String? = null,
    val mfaMethod: MfaMethod? = null,
    val mfaMaskedDestination: String? = null
)