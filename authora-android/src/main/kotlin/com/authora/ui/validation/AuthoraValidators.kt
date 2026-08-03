package com.authora.ui.validation

import com.authora.core.i18n.AuthoraStrings

object AuthoraValidators {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")

    fun validateEmail(value: String, strings: AuthoraStrings): String? {
        if (value.isBlank()) return strings.validationRequiredTemplate.format(strings.emailLabel)
        if (!emailRegex.matches(value)) return strings.validationEmailInvalid
        return null
    }

    fun validatePassword(value: String, strings: AuthoraStrings): String? {
        if (value.isBlank()) return strings.validationRequiredTemplate.format(strings.passwordLabel)
        if (value.length < 8) return strings.validationPasswordTooShort
        if (value.none { it.isDigit() }) return strings.validationPasswordNeedsDigit
        if (value.none { it.isLetter() }) return strings.validationPasswordNeedsLetter
        return null
    }

    fun validateConfirmPassword(password: String, confirmPassword: String, strings: AuthoraStrings): String? {
        if (confirmPassword.isBlank()) return strings.validationConfirmPasswordRequired
        if (confirmPassword != password) return strings.validationConfirmPasswordMismatch
        return null
    }

    fun validateFullName(value: String, strings: AuthoraStrings): String? {
        if (value.isBlank()) return strings.validationRequiredTemplate.format(strings.fullNameLabel)
        if (value.trim().length < 2) return strings.validationFullNameTooShort
        return null
    }
}