package com.authora.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.authora.ui.component.AuthoraPasswordField
import com.authora.ui.component.AuthoraPrimaryButton
import com.authora.ui.component.AuthoraTextField
import com.authora.ui.i18n.LocalAuthoraStrings

@Composable
fun SignUpScreen(
    authProvider: AuthoraAuthProvider,
    sessionStore: AuthoraSessionStore,
    providerType: String,
    onSignUpSuccess: () -> Unit,
    onNavigateToSignIn: () -> Unit,
    onMfaRequired: (challengeId: String, method: MfaMethod, maskedDestination: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = LocalAuthoraStrings.current
    val viewModel: SignUpViewModel = viewModel(
        factory = SignUpViewModelFactory(authProvider, sessionStore, providerType, strings)
    )
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.isSignedUp) {
        if (uiState.isSignedUp) onSignUpSuccess()
    }

    LaunchedEffect(uiState.mfaChallengeId) {
        val challengeId = uiState.mfaChallengeId
        val method = uiState.mfaMethod
        if (challengeId != null && method != null) {
            onMfaRequired(challengeId, method, uiState.mfaMaskedDestination)
            viewModel.onMfaHandled()
        }
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = strings.signUpTitle, style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(24.dp))

            AuthoraTextField(
                value = uiState.fullName,
                onValueChange = viewModel::onFullNameChange,
                label = strings.fullNameLabel,
                errorText = uiState.fullNameError,
                keyboardType = KeyboardType.Text
            )

            Spacer(modifier = Modifier.height(12.dp))

            AuthoraTextField(
                value = uiState.email,
                onValueChange = viewModel::onEmailChange,
                label = strings.emailLabel,
                errorText = uiState.emailError,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(12.dp))

            AuthoraPasswordField(
                value = uiState.password,
                onValueChange = viewModel::onPasswordChange,
                label = strings.passwordLabel,
                errorText = uiState.passwordError
            )

            Spacer(modifier = Modifier.height(12.dp))

            AuthoraPasswordField(
                value = uiState.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = strings.confirmPasswordLabel,
                errorText = uiState.confirmPasswordError
            )

            Spacer(modifier = Modifier.height(24.dp))

            AuthoraPrimaryButton(
                text = strings.signUpButton,
                onClick = viewModel::submit,
                isLoading = uiState.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = onNavigateToSignIn,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(strings.signUpHaveAccount)
            }
        }
    }
}