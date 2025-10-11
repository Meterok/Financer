package com.potaninpm.feature_auth.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun InputField(
    value: String,
    label: String? = null,
    leadingIcon: ImageVector? = null,
    onValueChange: (String) -> Unit,
    error: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label?.let { { Text(text = it) } },
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        leadingIcon = leadingIcon?.let {
            { Icon(imageVector = it, contentDescription = null) }
        },
        isError = error != null,
        singleLine = singleLine,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        supportingText = error?.let {
            { Text(text = it, color = MaterialTheme.colorScheme.error) }
        }
    )
}
