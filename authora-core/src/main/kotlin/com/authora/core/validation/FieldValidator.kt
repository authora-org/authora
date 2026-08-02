package com.authora.core.validation

import com.authora.core.config.FieldCatalog
import com.authora.core.config.FieldConfig
import com.authora.core.config.FieldType

object FieldValidator {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")
    private val urlRegex = Regex("^https?://[\\w.-]+(:\\d+)?(/.*)?$")
    private val phoneRegex = Regex("^\\+?[0-9]{7,15}$")
    private val usernameRegex = Regex("^[a-zA-Z0-9_]{3,20}$")

    fun validate(field: FieldConfig, value: String): String? {
        val label = field.label ?: FieldCatalog.labelFor(field.name)

        if (field.required && value.isBlank()) return "$label is required"
        if (value.isBlank()) return null

        return when (field.type) {
            FieldType.EMAIL -> if (!emailRegex.matches(value)) "Enter a valid email address" else null
            FieldType.PASSWORD -> validatePassword(value)
            FieldType.PHONE -> if (!phoneRegex.matches(value)) "Enter a valid phone number" else null
            FieldType.URL -> if (!urlRegex.matches(value)) "Enter a valid URL" else null
            FieldType.USERNAME -> if (!usernameRegex.matches(value)) "Username must be 3-20 characters (letters, numbers, underscore)" else null
            FieldType.TEXT -> null
        }
    }

    private fun validatePassword(value: String): String? {
        if (value.length < 8) return "Password must be at least 8 characters"
        if (value.none { it.isDigit() }) return "Password must contain at least one number"
        if (value.none { it.isLetter() }) return "Password must contain at least one letter"
        return null
    }
}