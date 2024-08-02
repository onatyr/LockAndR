package com.oyr.lockandr.presentation.lock

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDateTime

@Composable
fun DateTimeComposable(textColor: Color) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val dateTime = remember {
            mutableStateOf(
                LocalDateTime.now()
            )
        }

        LaunchedEffect(key1 = true) {
            while (true) {
                delay(1000L) // delay for 1 second
                dateTime.value = LocalDateTime.now()
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${
                    dateTime.value.hour.toString().padStart(2, '0')
                }:${dateTime.value.minute.toString().padStart(2, '0')}",
                fontSize = 60.sp,
                color = textColor
            )
            Text(
                text = "${dateTime.value.dayOfWeek} ${dateTime.value.dayOfMonth} ${dateTime.value.month}",
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraLight,
                color = textColor
            )
        }

    }
}