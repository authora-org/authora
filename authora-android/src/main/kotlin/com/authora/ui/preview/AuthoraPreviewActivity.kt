package com.authora.ui.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.authora.auth.MfaMethod
import com.authora.core.session.AuthoraSessionStore
import com.authora.ui.account.AccountScreen
import com.authora.ui.accountselector.AccountSelectorScreen
import com.authora.ui.mfa.MfaScreen
import com.authora.ui.signin.SignInScreen
import com.authora.ui.signup.SignUpScreen
import com.authora.ui.theme.AuthoraTheme

class AuthoraPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthoraTheme {
                val authProvider = remember { PreviewAuthProvider() }
                val sessionStore = remember { AuthoraSessionStore(applicationContext, "authora_preview_sessions") }

                var destination by remember { mutableStateOf(PlaygroundDestination.HOME) }
                var pendingMfaChallenge by remember { mutableStateOf<Triple<String, MfaMethod, String?>?>(null) }

                when (destination) {
                    PlaygroundDestination.HOME -> PlaygroundScreen(
                        onNavigate = { destination = it }
                    )
                    PlaygroundDestination.SIGN_IN -> SignInScreen(
                        authProvider = authProvider,
                        sessionStore = sessionStore,
                        providerType = "preview",
                        onSignInSuccess = { destination = PlaygroundDestination.ACCOUNT },
                        onNavigateToSignUp = { destination = PlaygroundDestination.SIGN_UP },
                        onMfaRequired = { challengeId, method, masked ->
                            pendingMfaChallenge = Triple(challengeId, method, masked)
                            destination = PlaygroundDestination.MFA
                        }
                    )
                    PlaygroundDestination.SIGN_UP -> SignUpScreen(
                        authProvider = authProvider,
                        sessionStore = sessionStore,
                        providerType = "preview",
                        onSignUpSuccess = { destination = PlaygroundDestination.ACCOUNT },
                        onNavigateToSignIn = { destination = PlaygroundDestination.SIGN_IN },
                        onMfaRequired = { challengeId, method, masked ->
                            pendingMfaChallenge = Triple(challengeId, method, masked)
                            destination = PlaygroundDestination.MFA
                        }
                    )
                    PlaygroundDestination.ACCOUNT -> AccountScreen(
                        authProvider = authProvider,
                        sessionStore = sessionStore,
                        onSignedOut = { destination = PlaygroundDestination.HOME },
                        onManageAccounts = { destination = PlaygroundDestination.ACCOUNT_SELECTOR }
                    )
                    PlaygroundDestination.ACCOUNT_SELECTOR -> AccountSelectorScreen(
                        sessionStore = sessionStore,
                        onAccountSelected = { destination = PlaygroundDestination.ACCOUNT },
                        onAddAccount = { destination = PlaygroundDestination.SIGN_IN }
                    )
                    PlaygroundDestination.MFA -> {
                        val challenge = pendingMfaChallenge
                        if (challenge != null) {
                            MfaScreen(
                                authProvider = authProvider,
                                sessionStore = sessionStore,
                                providerType = "preview",
                                challengeId = challenge.first,
                                method = challenge.second,
                                maskedDestination = challenge.third,
                                onVerified = { destination = PlaygroundDestination.ACCOUNT }
                            )
                        } else {
                            destination = PlaygroundDestination.HOME
                        }
                    }
                    PlaygroundDestination.INSPECTOR -> InspectorScreen()
                }
            }
        }
    }
}