package com.example.to_dolistjetpack.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.to_dolistjetpack.ui.theme.LightBlue

@Composable
fun TaskButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = {
        },
        modifier = modifier.height(40.dp),
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = LightBlue,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(10.dp)
    ) {
        Text(text = text)
    }
}