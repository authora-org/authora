package com.authora.ui.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.authora.ui.component.AuthoraCustomField

@Composable
fun AuthoraCustomFieldsForm(
    state: DynamicFormState,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        state.fields.forEachIndexed { index, field ->
            AuthoraCustomField(
                field = field,
                value = state.values[field.name].orEmpty(),
                onValueChange = { state.onValueChange(field.name, it) },
                errorText = state.errors[field.name]
            )
            if (index != state.fields.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}