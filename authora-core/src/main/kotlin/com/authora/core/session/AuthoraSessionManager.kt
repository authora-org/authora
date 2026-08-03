package com.authora.core.session

import com.authora.auth.AuthoraAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthoraSessionManager(
    private val authProvider: AuthoraAuthProvider,
    private val sessionStore: AuthoraSessionStore,
    private val providerType: String,
    private val scope: CoroutineScope
) {
    private val _state = MutableStateFlow<AuthoraSessionState>(AuthoraSessionState.Loading)
    val state: StateFlow<AuthoraSessionState> = _state.asStateFlow()

    private var started = false

    fun start() {
        if (started) return
        started = true
        refresh()
    }

    fun refresh() {
        scope.launch(Dispatchers.Default) {
            restore()
        }
    }

    private fun restore() {
        val rememberLogin = sessionStore.isRememberLoginEnabled()

        if (!rememberLogin) {
            scope.launch {
                authProvider.signOut()
                sessionStore.clear()
                _state.value = AuthoraSessionState.SignedOut
            }
            return
        }

        val currentUser = authProvider.currentUser()
        if (currentUser == null) {
            sessionStore.getActiveSession()?.let { sessionStore.removeSession(it.userId) }
            _state.value = AuthoraSessionState.SignedOut
            return
        }

        val session = AuthoraSession(
            userId = currentUser.id,
            email = currentUser.email,
            displayName = currentUser.displayName,
            providerType = providerType,
            isActive = true
        )
        sessionStore.saveSession(session)
        _state.value = AuthoraSessionState.SignedIn(session)
    }

    fun onSignedIn(session: AuthoraSession) {
        _state.value = AuthoraSessionState.SignedIn(session)
    }

    fun onSignedOut() {
        _state.value = AuthoraSessionState.SignedOut
    }

    fun setRememberLogin(enabled: Boolean) {
        sessionStore.setRememberLoginEnabled(enabled)
        if (!enabled) {
            sessionStore.getAllSessions().forEach { sessionStore.removeSession(it.userId) }
        }
    }

    fun isRememberLoginEnabled(): Boolean = sessionStore.isRememberLoginEnabled()
}