package com.authora.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.KeyboardType
import com.authora.core.config.FieldCatalog
import com.authora.core.config.FieldConfig
import com.authora.core.config.FieldType

@Composable
fun AuthoraCustomField(
    field: FieldConfig,
    value: String,
    onValueChange: (String) -> Unit,
    errorText: String?
) {
    val label = field.label ?: FieldCatalog.labelFor(field.name)

    when (field.type) {
        FieldType.PASSWORD -> AuthoraPasswordField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            errorText = errorText
        )
        else -> AuthoraTextField(
            value = value,
            onValueChange = onValueChange,
            label = label,
            errorText = errorText,
            keyboardType = when (field.type) {
                FieldType.EMAIL -> KeyboardType.Email
                FieldType.PHONE -> KeyboardType.Phone
                FieldType.URL -> KeyboardType.Uri
                else -> KeyboardType.Text
            }
        )
    }
}