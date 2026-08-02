package com.authora.ui.account

import com.authora.auth.AuthoraUser

data class AccountUiState(
    val user: AuthoraUser? = null,
    val isSignedOut: Boolean = false,
    val snackbarMessage: String? = null
)