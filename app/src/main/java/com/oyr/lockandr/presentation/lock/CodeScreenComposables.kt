package com.oyr.lockandr.presentation.lock

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun DigitKeyboard(
    modifier: Modifier,
    onDigitClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onValidateClick: () -> Unit
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val keys = listOf(
            "1", "2", "3",
            "4", "5", "6",
            "7", "8", "9",
            "back", "0", "validate"
        )

        keys.chunked(3).forEach { rowKeys ->
            Row(
                modifier = Modifier
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(40.dp)
            ) {
                rowKeys.forEach { key ->
                    when (key) {
                        "back" -> BackButton(onBackClick)
                        "validate" -> ValidateButton(onValidateClick)
                        else -> DigitButton(key, onDigitClick)
                    }
                }
            }
        }
    }
}

@Composable
fun KeyboardButton(onButtonClick: () -> Unit, buttonContent: @Composable () -> Unit) {
    val vibrator = LocalContext.current.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    val effect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)

    CompositionLocalProvider(LocalRippleTheme provides MyRippleTheme()) {
        Button(
            modifier = Modifier.size(70.dp),
            shape = CircleShape,
            border = BorderStroke(0.5.dp, Color.Gray),
            colors = ButtonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            onClick = {
                vibrator.vibrate(effect)
                onButtonClick()
            }) {
            buttonContent()
        }
    }
}

@Composable
fun DigitButton(value: String, onButtonClick: (String) -> Unit) {
    KeyboardButton(
        onButtonClick = { onButtonClick(value) }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(text = value)
        }
    }
}

@Composable
fun BackButton(onButtonClick: () -> Unit) {
    KeyboardButton(
        onButtonClick = { onButtonClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "delete last"
            )
        }
    }
}

@Composable
fun ValidateButton(onButtonClick: () -> Unit) {
    KeyboardButton(
        onButtonClick = { onButtonClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send,
                contentDescription = "validate input"
            )
        }
    }
}

class MyRippleTheme : RippleTheme {

    @Composable
    override fun defaultColor() = Color.White

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleTheme.defaultRippleAlpha(
        Color.White,
        lightTheme = true
    )
}
