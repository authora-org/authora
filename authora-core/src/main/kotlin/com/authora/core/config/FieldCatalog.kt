package com.authora.core.config

object FieldCatalog {
    private val defaultLabels = mapOf(
        "username" to "Username",
        "email" to "Email",
        "password" to "Password",
        "confirm_password" to "Confirm Password",
        "phone" to "Phone Number",
        "full_name" to "Full Name",
        "display_name" to "Display Name",
        "bio" to "Bio",
        "website" to "Website",
        "country" to "Country",
        "github" to "GitHub",
        "microsoft" to "Microsoft",
        "discord" to "Discord",
        "telegram" to "Telegram",
        "linkedin" to "LinkedIn"
    )

    private val requiredByDefault = setOf("username", "email", "password", "confirm_password", "full_name")

    fun labelFor(name: String): String =
        defaultLabels[name] ?: name.replace("_", " ").replaceFirstChar { it.uppercase() }

    fun isRequiredByDefault(name: String): Boolean = name in requiredByDefault
}