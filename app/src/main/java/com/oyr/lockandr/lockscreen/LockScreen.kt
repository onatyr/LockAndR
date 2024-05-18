package com.oyr.lockandr.lockscreen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext


@Composable
fun LockScreen(viewModel: LockViewModel) {
    val context = LocalContext.current
    val wallpaper = viewModel.getDeviceWallpaper(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
//            .paint(
//                painter = (if (wallpaper != null) BitmapPainter(wallpaper) else BitmapPainter(
//                    Bitmap
//                        .createBitmap(
//                            context.resources.displayMetrics.widthPixels,
//                            context.resources.displayMetrics.heightPixels,
//                            Bitmap.Config.ARGB_8888
//                        )
//                        .asImageBitmap()
//                ))
//            )
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
        }) {
            Text(text = "UNLOCK")
        }
    }
}