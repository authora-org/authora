package com.authora.ui.session

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.authora.core.session.AuthoraSession
import com.authora.core.session.AuthoraSessionManager
import com.authora.core.session.AuthoraSessionState

@Composable
fun AuthoraSessionGate(
    sessionManager: AuthoraSessionManager,
    signedOutContent: @Composable () -> Unit,
    signedInContent: @Composable (AuthoraSession) -> Unit
) {
    val state by sessionManager.state.collectAsState()

    DisposableEffect(sessionManager) {
        sessionManager.start()
        onDispose { }
    }

    when (val current = state) {
        is AuthoraSessionState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AuthoraSessionState.SignedOut -> signedOutContent()
        is AuthoraSessionState.SignedIn -> signedInContent(current.session)
    }
}