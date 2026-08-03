package com.authora.ui.mfa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.authora.auth.AuthoraAuthProvider
import com.authora.core.i18n.AuthoraStrings
import com.authora.core.session.AuthoraSessionStore

class MfaViewModelFactory(
    private val authProvider: AuthoraAuthProvider,
    private val sessionStore: AuthoraSessionStore,
    private val challengeId: String,
    private val providerType: String,
    private val strings: AuthoraStrings
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MfaViewModel(authProvider, sessionStore, challengeId, providerType, strings) as T
    }
}