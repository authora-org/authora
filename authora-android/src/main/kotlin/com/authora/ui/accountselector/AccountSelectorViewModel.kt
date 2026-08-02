package com.authora.ui.accountselector

import androidx.lifecycle.ViewModel
import com.authora.core.session.AuthoraSessionStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AccountSelectorViewModel(
    private val sessionStore: AuthoraSessionStore
) : ViewModel() {

    private val _uiState = MutableStateFlow(AccountSelectorUiState(sessions = sessionStore.getAllSessions()))
    val uiState: StateFlow<AccountSelectorUiState> = _uiState.asStateFlow()

    fun refresh() {
        _uiState.update { it.copy(sessions = sessionStore.getAllSessions()) }
    }

    fun switchTo(userId: String) {
        sessionStore.setActiveSession(userId)
        _uiState.update { it.copy(sessions = sessionStore.getAllSessions(), switchedToUserId = userId) }
    }

    fun remove(userId: String) {
        sessionStore.removeSession(userId)
        refresh()
    }

    fun onSwitchHandled() {
        _uiState.update { it.copy(switchedToUserId = null) }
    }
}