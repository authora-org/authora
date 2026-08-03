package com.authora.core.validation

import com.authora.core.config.FieldCatalog
import com.authora.core.config.FieldConfig
import com.authora.core.config.FieldType
import com.authora.core.i18n.AuthoraStrings

object FieldValidator {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")
    private val urlRegex = Regex("^https?://[\\w.-]+(:\\d+)?(/.*)?$")
    private val phoneRegex = Regex("^\\+?[0-9]{7,15}$")
    private val usernameRegex = Regex("^[a-zA-Z0-9_]{3,20}$")

    fun validate(field: FieldConfig, value: String, strings: AuthoraStrings): String? {
        val label = field.label ?: FieldCatalog.labelFor(field.name)

        if (field.required && value.isBlank()) {
            return strings.validationRequiredTemplate.format(label)
        }
        if (value.isBlank()) return null

        return when (field.type) {
            FieldType.EMAIL -> if (!emailRegex.matches(value)) strings.validationEmailInvalid else null
            FieldType.PASSWORD -> validatePassword(value, strings)
            FieldType.PHONE -> if (!phoneRegex.matches(value)) strings.validationPhoneInvalid else null
            FieldType.URL -> if (!urlRegex.matches(value)) strings.validationUrlInvalid else null
            FieldType.USERNAME -> if (!usernameRegex.matches(value)) strings.validationUsernameInvalid else null
            FieldType.TEXT -> null
        }
    }

    private fun validatePassword(value: String, strings: AuthoraStrings): String? {
        if (value.length < 8) return strings.validationPasswordTooShort
        if (value.none { it.isDigit() }) return strings.validationPasswordNeedsDigit
        if (value.none { it.isLetter() }) return strings.validationPasswordNeedsLetter
        return null
    }
}