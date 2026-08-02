package com.authora.ui.validation

object AuthoraValidators {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$")

    fun validateEmail(value: String): String? {
        if (value.isBlank()) return "Email is required"
        if (!emailRegex.matches(value)) return "Enter a valid email address"
        return null
    }

    fun validatePassword(value: String): String? {
        if (value.isBlank()) return "Password is required"
        if (value.length < 8) return "Password must be at least 8 characters"
        if (value.none { it.isDigit() }) return "Password must contain at least one number"
        if (value.none { it.isLetter() }) return "Password must contain at least one letter"
        return null
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): String? {
        if (confirmPassword.isBlank()) return "Please confirm your password"
        if (confirmPassword != password) return "Passwords do not match"
        return null
    }

    fun validateFullName(value: String): String? {
        if (value.isBlank()) return "Full name is required"
        if (value.trim().length < 2) return "Full name is too short"
        return null
    }
}