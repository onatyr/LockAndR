package com.oyr.lockandr.lockscreen

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


@Composable
fun LockScreen(viewModel: LockViewModel) {
    val context = LocalContext.current
    val wallpaper = viewModel.getDeviceWallpaper(context)

    LockBackground(wallpaper) {
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp)
                    .aspectRatio(18f),
                tint = Color.White
            )
            Box(modifier = Modifier.align(Alignment.TopCenter).padding(top = 100.dp)) {
                DateTimeComposable()
            }
            Button(
                onClick = {
                    viewModel.unlock()
                }, modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .background(Color.Black)
                    .align(Alignment.Center)
            ) {
                Text(text = "UNLOCK")
            }
            Text(
                text = "Faites glisser pour déverrouiller",
                modifier = Modifier.align(Alignment.BottomCenter),
                color = Color.White
            )
        }

    }
}

@Composable
fun LockBackground(wallpaper: ImageBitmap?, content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black, shape = RectangleShape)
    ) {
        if (wallpaper != null) {
            Image(
                painter = BitmapPainter(wallpaper),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight
            )
        }

        content()
    }
}

@Composable
fun DateTimeComposable() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val dateTime = remember {
            mutableStateOf(
                LocalDateTime.now()
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
            )
        }

        LaunchedEffect(key1 = true) {
            while (true) {
                delay(1000L) // delay for 1 second
                dateTime.value = LocalDateTime.now()
                    .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
            }
        }

        Column {
            Text(
                text = dateTime.value.split(", ")[1].split(":").dropLast(1).joinToString(":"),
                fontSize = 60.sp,
                color = Color.White
            )
            Text(
                text = dateTime.value.split(", ")[0],
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraLight,
                color = Color.White
            )
        }

    }
}