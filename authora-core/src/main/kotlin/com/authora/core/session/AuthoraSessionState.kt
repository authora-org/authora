package com.authora.core.session

sealed class AuthoraSessionState {
    data object Loading : AuthoraSessionState()
    data class SignedIn(val session: AuthoraSession) : AuthoraSessionState()
    data object SignedOut : AuthoraSessionState()
}