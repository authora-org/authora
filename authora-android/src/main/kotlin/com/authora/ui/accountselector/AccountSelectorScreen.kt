package com.authora.ui.accountselector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.authora.core.session.AuthoraSessionStore
import com.authora.ui.component.AuthoraAvatar

@Composable
fun AccountSelectorScreen(
    sessionStore: AuthoraSessionStore,
    onAccountSelected: (String) -> Unit,
    onAddAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: AccountSelectorViewModel = viewModel(factory = AccountSelectorViewModelFactory(sessionStore))
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.switchedToUserId) {
        uiState.switchedToUserId?.let {
            onAccountSelected(it)
            viewModel.onSwitchHandled()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Switch Account") }) }
    ) { padding ->
        LazyColumn(modifier = modifier.fillMaxSize().padding(padding)) {
            items(uiState.sessions, key = { it.userId }) { session ->
                ListItem(
                    headlineContent = { Text(session.displayName ?: session.email) },
                    supportingContent = { Text(session.email) },
                    leadingContent = { AuthoraAvatar(label = session.displayName ?: session.email) },
                    trailingContent = {
                        Row {
                            if (session.isActive) {
                                Icon(imageVector = Icons.Filled.Check, contentDescription = "Active account")
                            }
                            IconButton(onClick = { viewModel.remove(session.userId) }) {
                                Icon(imageVector = Icons.Filled.Close, contentDescription = "Remove account")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.switchTo(session.userId) }
                )
            }

            item {
                ListItem(
                    headlineContent = { Text("Add account") },
                    leadingContent = { Icon(imageVector = Icons.Filled.PersonAdd, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onAddAccount() }
                )
            }
        }
    }
}