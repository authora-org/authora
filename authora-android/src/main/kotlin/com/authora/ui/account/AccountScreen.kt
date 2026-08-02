package com.authora.ui.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.authora.auth.AuthoraAuthProvider
import com.authora.core.session.AuthoraSessionStore
import com.authora.ui.profile.ProfileCard

@Composable
fun AccountScreen(
    authProvider: AuthoraAuthProvider,
    sessionStore: AuthoraSessionStore,
    onSignedOut: () -> Unit,
    onManageAccounts: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: AccountViewModel = viewModel(factory = AccountViewModelFactory(authProvider, sessionStore))
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSignedOut) {
        if (uiState.isSignedOut) onSignedOut()
    }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onSnackbarShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            uiState.user?.let { user ->
                ProfileCard(user = user)
            }

            Divider()

            ListItem(
                headlineContent = { Text("Manage accounts") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onManageAccounts() }
            )

            Divider()

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = viewModel::signOut,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text("Sign Out")
            }
        }
    }
}