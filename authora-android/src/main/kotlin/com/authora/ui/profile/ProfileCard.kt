package com.authora.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.authora.auth.AuthoraUser
import com.authora.ui.component.AuthoraAvatar

@Composable
fun ProfileCard(
    user: AuthoraUser,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AuthoraAvatar(label = user.displayName ?: user.email)
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = user.displayName ?: user.email, style = MaterialTheme.typography.titleMedium)
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}