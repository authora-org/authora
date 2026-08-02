package com.authora.core.provider

import com.authora.auth.AuthoraAuthProvider

object AuthoraProviderRegistry {
    private val factories = mutableMapOf<String, () -> AuthoraAuthProvider>()

    fun register(type: String, factory: () -> AuthoraAuthProvider) {
        factories[type] = factory
    }

    fun create(type: String): AuthoraAuthProvider {
        val factory = factories[type]
            ?: throw IllegalStateException(
                "No Authora provider registered for type '$type'. Did you forget to call AuthoraProviderRegistry.register(...)?"
            )
        return factory()
    }

    fun isRegistered(type: String): Boolean = factories.containsKey(type)
}