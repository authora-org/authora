package com.authora.ui.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import com.authora.core.config.FieldConfig
import com.authora.core.config.FieldType
import com.authora.core.i18n.AuthoraStrings
import com.authora.core.validation.FieldValidator
import com.authora.ui.i18n.LocalAuthoraStrings

class DynamicFormState(
    val fields: List<FieldConfig>,
    private val strings: AuthoraStrings
) {
    val values = mutableStateMapOf<String, String>().apply {
        fields.forEach { put(it.name, "") }
    }
    val errors = mutableStateMapOf<String, String?>()

    fun onValueChange(fieldName: String, value: String) {
        values[fieldName] = value
        errors[fieldName] = null
    }

    fun validateAll(): Boolean {
        var isValid = true
        fields.forEach { field ->
            val error = FieldValidator.validate(field, values[field.name].orEmpty(), strings)
            errors[field.name] = error
            if (error != null) isValid = false
        }

        val hasPassword = fields.any { it.name == "password" && it.type == FieldType.PASSWORD }
        val hasConfirm = fields.any { it.name == "confirm_password" }
        if (hasPassword && hasConfirm) {
            val passwordValue = values["password"].orEmpty()
            val confirmValue = values["confirm_password"].orEmpty()
            if (confirmValue != passwordValue) {
                errors["confirm_password"] = strings.validationConfirmPasswordMismatch
                isValid = false
            }
        }

        return isValid
    }
}

@Composable
fun rememberDynamicFormState(fields: List<FieldConfig>): DynamicFormState {
    val strings = LocalAuthoraStrings.current
    return remember(fields, strings) { DynamicFormState(fields, strings) }
}