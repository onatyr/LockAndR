package com.oyr.lockandr.lockscreen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext


@Composable
fun LockScreen(viewModel: LockViewModel) {
    Log.e("debuglock", "LOCKSCREEN")
    val context = LocalContext.current
    val text = remember {
        mutableStateOf("UNLOCK")
    }
//    val wallpaper = viewModel.getDeviceWallpaper(context)
    val wallpaper: ImageBitmap? = null
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (wallpaper != null) {
            Image(
                painter = BitmapPainter(wallpaper),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillHeight
            )
        }
        Button(onClick = {
            viewModel.unlock()
            text.value = "LOCKED"
        }, modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)) {
            Text(text = text.value)
        }
    }
}