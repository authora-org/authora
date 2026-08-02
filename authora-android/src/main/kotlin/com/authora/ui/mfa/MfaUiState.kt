package com.authora.ui.mfa

data class MfaUiState(
    val code: String = "",
    val codeError: String? = null,
    val isVerifying: Boolean = false,
    val isResending: Boolean = false,
    val isVerified: Boolean = false,
    val snackbarMessage: String? = null
)