package com.authora.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.authora.auth.AuthoraAuthProvider
import com.authora.core.session.AuthoraSessionStore

class SignUpViewModelFactory(
    private val authProvider: AuthoraAuthProvider,
    private val sessionStore: AuthoraSessionStore,
    private val providerType: String
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignUpViewModel(authProvider, sessionStore, providerType) as T
    }
}