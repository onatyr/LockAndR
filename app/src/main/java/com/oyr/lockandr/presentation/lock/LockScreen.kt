package com.oyr.lockandr.presentation.lock

import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.oyr.lockandr.R


@Composable
fun LockScreen(viewModel: LockViewModel) {
    val context = LocalContext.current
    val wallpaper =
        viewModel.getDeviceWallpaper(context)!!
    val displayedScreen = viewModel.displayedBox.collectAsState()

    LockBackground(wallpaper, viewModel::initOffsetY, viewModel::updateOffsetY) {
        SaveScreen(displayedScreen = displayedScreen.value, offsetYStateFlow = viewModel.offsetY)
        CodeScreen(displayedScreen = displayedScreen.value, viewModel)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LockBackground(
    wallpaper: ImageBitmap,
    initOffset: (Float) -> Unit,
    updateOffset: (Float) -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black, shape = RectangleShape)
            .pointerInteropFilter {
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> initOffset(it.y)
                    MotionEvent.ACTION_MOVE -> updateOffset(it.y)
                    MotionEvent.ACTION_UP -> initOffset(0f)
                }
                true
            }
    ) {
        Image(
            painter = painterResource(id = R.drawable.wallpaper),
            contentDescription = "Background",
            modifier = Modifier.fillMaxHeight(),
            contentScale = ContentScale.Crop
        )
    }

    content()
}