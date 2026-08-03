package com.authora.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authora.auth.AuthResult
import com.authora.auth.AuthoraAuthProvider
import com.authora.core.i18n.AuthoraStrings
import com.authora.core.session.AuthoraSession
import com.authora.core.session.AuthoraSessionStore
import com.authora.ui.validation.AuthoraValidators
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authProvider: AuthoraAuthProvider,
    private val sessionStore: AuthoraSessionStore,
    private val providerType: String,
    private val strings: AuthoraStrings
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun onFullNameChange(value: String) {
        _uiState.update { it.copy(fullName = value, fullNameError = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, emailError = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, confirmPasswordError = null) }
    }

    fun onConfirmPasswordChange(value: String) {
        _uiState.update { it.copy(confirmPassword = value, confirmPasswordError = null) }
    }

    fun onSnackbarShown() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun onMfaHandled() {
        _uiState.update { it.copy(mfaChallengeId = null, mfaMethod = null, mfaMaskedDestination = null) }
    }

    fun submit() {
        val state = _uiState.value
        val fullNameError = AuthoraValidators.validateFullName(state.fullName, strings)
        val emailError = AuthoraValidators.validateEmail(state.email, strings)
        val passwordError = AuthoraValidators.validatePassword(state.password, strings)
        val confirmPasswordError = AuthoraValidators.validateConfirmPassword(state.password, state.confirmPassword, strings)

        if (fullNameError != null || emailError != null || passwordError != null || confirmPasswordError != null) {
            _uiState.update {
                it.copy(
                    fullNameError = fullNameError,
                    emailError = emailError,
                    passwordError = passwordError,
                    confirmPasswordError = confirmPasswordError
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            when (val result = authProvider.signUp(state.fullName.trim(), state.email.trim(), state.password)) {
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
                    _uiState.update { it.copy(isLoading = false, isSignedUp = true) }
                }
                is AuthResult.Failure -> _uiState.update {
                    it.copy(isLoading = false, snackbarMessage = result.message)
                }
                is AuthResult.RequiresMfa -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        mfaChallengeId = result.challengeId,
                        mfaMethod = result.method,
                        mfaMaskedDestination = result.maskedDestination
                    )
                }
            }
        }
    }
}