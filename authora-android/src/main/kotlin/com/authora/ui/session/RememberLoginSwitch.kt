package com.authora.ui.session

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.authora.core.session.AuthoraSessionManager

@Composable
fun RememberLoginSwitch(
    sessionManager: AuthoraSessionManager,
    modifier: Modifier = Modifier
) {
    var enabled by remember { mutableStateOf(sessionManager.isRememberLoginEnabled()) }

    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Remember me", modifier = Modifier.weight(1f, fill = true))
        Switch(
            checked = enabled,
            onCheckedChange = {
                enabled = it
                sessionManager.setRememberLogin(it)
            }
        )
    }
}