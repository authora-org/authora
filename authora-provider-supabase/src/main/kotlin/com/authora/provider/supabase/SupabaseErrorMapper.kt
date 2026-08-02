package com.authora.provider.supabase

object SupabaseErrorMapper {
    fun toFriendlyMessage(throwable: Throwable): String {
        val message = throwable.message.orEmpty()
        return when {
            message.contains("Invalid login credentials", ignoreCase = true) ->
                "The email or password you entered is incorrect."
            message.contains("User already registered", ignoreCase = true) ->
                "An account with this email already exists."
            message.contains("Password should be at least", ignoreCase = true) ->
                "This password is too weak. Please choose a stronger one."
            message.contains("network", ignoreCase = true) || message.contains("timeout", ignoreCase = true) ->
                "Network error. Please check your connection and try again."
            message.isBlank() -> "Something went wrong. Please try again."
            else -> message
        }
    }
}