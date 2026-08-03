package com.authora.ui.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.authora.core.config.AuthoraConfigException
import com.authora.core.config.AuthoraConfigLoader
import com.authora.core.session.AuthoraSessionStore

@Composable
fun InspectorScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val configText = remember {
        try {
            val config = AuthoraConfigLoader.load(context)
            buildString {
                appendLine("Authora version: ${config.version}")
                appendLine("Language: ${config.language}")
                appendLine("Provider: ${config.provider}")
                appendLine("App name: ${config.ui.appName}")
                appendLine("App icon: ${config.ui.appIcon}")
                appendLine("Dynamic color: ${config.ui.theme.dynamicColor}")
                appendLine("Dark mode: ${config.ui.theme.darkMode}")
                appendLine("Fields declared: ${config.fields.size}")
                config.fields.forEach { field ->
                    appendLine("  - ${field.name} (${field.type}, required=${field.required})")
                }
            }
        } catch (e: AuthoraConfigException) {
            "Could not load Authora.toml: ${e.message}"
        }
    }

    val sessionText = remember {
        val sessionStore = AuthoraSessionStore(context)
        val sessions = sessionStore.getAllSessions()
        if (sessions.isEmpty()) {
            "No stored sessions."
        } else {
            buildString {
                sessions.forEach { session ->
                    appendLine("${session.email} (${session.providerType})${if (session.isActive) " — active" else ""}")
                }
            }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Authora Inspector") }) }
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Configuration", style = MaterialTheme.typography.titleMedium)
            Text(text = configText, style = MaterialTheme.typography.bodySmall)

            Divider(modifier = Modifier.padding(vertical = 16.dp))

            Text(text = "Stored Sessions", style = MaterialTheme.typography.titleMedium)
            Text(text = sessionText, style = MaterialTheme.typography.bodySmall)
        }
    }
}