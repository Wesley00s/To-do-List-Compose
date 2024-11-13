package com.example.to_dolistjetpack.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.to_dolistjetpack.R
import com.example.to_dolistjetpack.ui.theme.LightBlue
import com.example.to_dolistjetpack.ui.theme.MediumGreen

@Composable
fun TaskTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    label: String,
    maxLines: Int,
    keyboardType: KeyboardType,
    singleLine: Boolean,
    leadingIcon: ImageVector = ImageVector.vectorResource(id = R.drawable.ic_default_input),
    focusedBorderColor: Color = LightBlue,
    unfocusedBorderColor: Color = Color.LightGray,
    errorBorderColor: Color = Color.Red,
    successBorderColor: Color = MediumGreen,
    isPassword: Boolean = false,
    isValid: (String) -> Boolean = { true },
    errorMessage: String = "Enter a valid value"
) {
    val icon = when (keyboardType) {
        KeyboardType.Password -> ImageVector.vectorResource(id = R.drawable.ic_lock)
        KeyboardType.Email -> ImageVector.vectorResource(id = R.drawable.ic_email)
        else -> leadingIcon
    }

    var passwordVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    var isTouched by remember { mutableStateOf(false) }

    LaunchedEffect(isFocused) {
        if (isTouched.not() && !isFocused && value.isNotEmpty()) {
            isTouched = true
        }
    }

    val isFieldValid = isValid(value)

    val borderColor = when {
        isTouched && isFieldValid -> successBorderColor
        isTouched && !isFieldValid -> errorBorderColor
        isFocused -> focusedBorderColor
        else -> unfocusedBorderColor
    }

    val labelColor = when {
        isTouched && isFieldValid -> successBorderColor
        isTouched && !isFieldValid -> errorBorderColor
        isFocused -> focusedBorderColor
        else -> unfocusedBorderColor
    }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                isTouched = true
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = label, color = labelColor) },
            maxLines = maxLines,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = borderColor,
                focusedLabelColor = labelColor,
                unfocusedBorderColor = borderColor,
                unfocusedLabelColor = labelColor,
                cursorColor = LightBlue
            ),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType
            ),
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = singleLine,
            interactionSource = interactionSource,
            trailingIcon = {
                if (isPassword) {
                    val imgVector = if (passwordVisible)
                        ImageVector.vectorResource(id = R.drawable.ic_close_eye)
                    else
                        ImageVector.vectorResource(id = R.drawable.ic_open_eye)
                    val description = if (passwordVisible)
                        "Hide password"
                    else
                        "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Image(imageVector = imgVector, contentDescription = description)
                    }
                }
            },
            leadingIcon = {
                Image(imageVector = icon, contentDescription = "Field Icon")
            }
        )

        if (isTouched && !isFieldValid) {
            Text(
                text = errorMessage,
                color = errorBorderColor,
                style = TextStyle(fontSize = 13.sp),
                modifier = Modifier
                    .padding(start = 8.dp, top = 0.dp)
                    .offset(y = 8.dp)
            )
        }
    }
}
