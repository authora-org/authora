package com.authora.ui.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.authora.ui.component.AuthoraPrimaryButton

@Composable
fun PlaygroundScreen(
    onNavigate: (PlaygroundDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Authora Playground") }) }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Preview every Authora UI component with simulated data. No real network calls are made. " +
                    "Tip: use email \"mfa@authora.dev\" on Sign In to preview the MFA flow.",
                style = MaterialTheme.typography.bodyMedium
            )

            AuthoraPrimaryButton(text = "Sign In", onClick = { onNavigate(PlaygroundDestination.SIGN_IN) })
            AuthoraPrimaryButton(text = "Sign Up", onClick = { onNavigate(PlaygroundDestination.SIGN_UP) })
            AuthoraPrimaryButton(text = "Account", onClick = { onNavigate(PlaygroundDestination.ACCOUNT) })
            AuthoraPrimaryButton(text = "Account Selector", onClick = { onNavigate(PlaygroundDestination.ACCOUNT_SELECTOR) })
            AuthoraPrimaryButton(text = "MFA", onClick = { onNavigate(PlaygroundDestination.MFA) })
            AuthoraPrimaryButton(text = "Inspector", onClick = { onNavigate(PlaygroundDestination.INSPECTOR) })
        }
    }
}