package com.authora.core.session

import kotlinx.serialization.Serializable

@Serializable
data class AuthoraSession(
    val userId: String,
    val email: String,
    val displayName: String?,
    val providerType: String,
    val isActive: Boolean
)