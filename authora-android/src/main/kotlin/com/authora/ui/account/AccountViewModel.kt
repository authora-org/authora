package com.authora.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.authora.auth.AuthoraAuthProvider
import com.authora.core.session.AuthoraSessionStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val authProvider: AuthoraAuthProvider,
    private val sessionStore: AuthoraSessionStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountUiState(user = authProvider.currentUser()))
    val uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    fun onSnackbarShown() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }

    fun signOut() {
        viewModelScope.launch {
            val userId = _uiState.value.user?.id
            authProvider.signOut()
            if (userId != null) sessionStore.removeSession(userId)
            _uiState.update { it.copy(isSignedOut = true) }
        }
    }
}