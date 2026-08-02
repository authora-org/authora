package com.authora.ui.accountselector

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.authora.core.session.AuthoraSessionStore

class AccountSelectorViewModelFactory(
    private val sessionStore: AuthoraSessionStore
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountSelectorViewModel(sessionStore) as T
    }
}