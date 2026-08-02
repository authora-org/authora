package com.authora.ui.mfa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authora.auth.AuthResult
import com.authora.auth.AuthoraAuthProvider
import com.authora.core.session.AuthoraSession
import com.authora.core.session.AuthoraSessionStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MfaViewModel(
    private val authProvider: AuthoraAuthProvider,
    private val sessionStore: AuthoraSessionStore,
    private val challengeId: String,
    private val providerType: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(MfaUiState())
    val uiState: StateFlow<MfaUiState> = _uiState.asStateFlow()

    fun onCodeChange(value: String) {
        _uiState.update { it.copy(code = value, codeError = null) }
    }

    fun onSnackbarShown() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun verify() {
        val code = _uiState.value.code
        if (code.isBlank()) {
            _uiState.update { it.copy(codeError = "Verification code is required") }
            return
        }

        _uiState.update { it.copy(isVerifying = true) }

        viewModelScope.launch {
            when (val result = authProvider.verifyMfa(challengeId, code)) {
                is AuthResult.Success -> {
                    sessionStore.saveSession(
                        AuthoraSession(
                            userId = result.user.id,
                            email = result.user.email,
                            displayName = result.user.displayName,
                            providerType = providerType,
                            isActive = true
                        )
                    )
                    _uiState.update { it.copy(isVerifying = false, isVerified = true) }
                }
                is AuthResult.Failure -> _uiState.update {
                    it.copy(isVerifying = false, snackbarMessage = result.message)
                }
                is AuthResult.RequiresMfa -> _uiState.update {
                    it.copy(isVerifying = false, snackbarMessage = "Verification code was incorrect. Please try again.")
                }
            }
        }
    }

    fun resend() {
        _uiState.update { it.copy(isResending = true) }
        viewModelScope.launch {
            val success = authProvider.resendMfaCode(challengeId)
            _uiState.update {
                it.copy(
                    isResending = false,
                    snackbarMessage = if (success) "A new verification code was sent." else "Could not resend the code. Please try again."
                )
            }
        }
    }
}