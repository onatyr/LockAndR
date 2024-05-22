package com.oyr.lockandr.lockscreen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext


@Composable
fun LockScreen(viewModel: LockViewModel) {
    Log.e("debuglock", "LOCKSCREEN")
    val context = LocalContext.current
    val wallpaper = viewModel.getDeviceWallpaper(context)
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
        Button(
            onClick = {
             viewModel.unlock()
            }, modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(Color.Black)
                .align(Alignment.Center)
        ) {
            Text(text = "LOCKED")
        }
    }
}