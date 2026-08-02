package com.authora.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.authora.auth.AuthoraAuthProvider
import com.authora.core.session.AuthoraSessionStore

class AccountViewModelFactory(
    private val authProvider: AuthoraAuthProvider,
    private val sessionStore: AuthoraSessionStore
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountViewModel(authProvider, sessionStore) as T
    }
}