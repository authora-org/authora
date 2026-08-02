package com.authora.ui.mfa

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.authora.auth.AuthoraAuthProvider
import com.authora.auth.MfaMethod
import com.authora.core.session.AuthoraSessionStore
import com.authora.ui.component.AuthoraPrimaryButton
import com.authora.ui.component.AuthoraTextField

@Composable
fun MfaScreen(
    authProvider: AuthoraAuthProvider,
    sessionStore: AuthoraSessionStore,
    providerType: String,
    challengeId: String,
    method: MfaMethod,
    maskedDestination: String?,
    onVerified: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: MfaViewModel = viewModel(
        factory = MfaViewModelFactory(authProvider, sessionStore, challengeId, providerType)
    )
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isVerified) {
        if (uiState.isVerified) onVerified()
    }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onSnackbarShown()
        }
    }

    val description = when (method) {
        MfaMethod.SMS -> "Enter the code we sent to ${maskedDestination ?: "your phone"}"
        MfaMethod.EMAIL -> "Enter the code we sent to ${maskedDestination ?: "your email"}"
        MfaMethod.TOTP -> "Enter the code from your authenticator app"
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Verify Your Identity", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = description, style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(24.dp))

            AuthoraTextField(
                value = uiState.code,
                onValueChange = viewModel::onCodeChange,
                label = "Verification Code",
                errorText = uiState.codeError,
                keyboardType = KeyboardType.NumberPassword
            )

            Spacer(modifier = Modifier.height(24.dp))

            AuthoraPrimaryButton(
                text = "Verify",
                onClick = viewModel::verify,
                isLoading = uiState.isVerifying
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = viewModel::resend,
                enabled = !uiState.isResending,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (uiState.isResending) "Resending..." else "Resend Code")
            }
        }
    }
}