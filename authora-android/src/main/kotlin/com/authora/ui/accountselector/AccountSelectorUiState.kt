package com.authora.ui.accountselector

import com.authora.core.session.AuthoraSession

data class AccountSelectorUiState(
    val sessions: List<AuthoraSession> = emptyList(),
    val switchedToUserId: String? = null
)