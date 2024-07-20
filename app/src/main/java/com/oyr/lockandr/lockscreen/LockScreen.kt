package com.oyr.lockandr.lockscreen

import android.os.Build
import android.view.MotionEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oyr.lockandr.R
import kotlinx.coroutines.delay
import java.time.LocalDateTime


@Composable
fun LockScreen(viewModel: LockViewModel) {
    val context = LocalContext.current
    val wallpaper = viewModel.getDeviceWallpaper(context)
    val offsetY = viewModel.offsetY.collectAsState()
    val adaptativeFontSize: Float = 12f * (1f + when(offsetY.value) {
        in 0f..500f -> offsetY.value / 714.2f
        else -> { if (offsetY.value <= 0) 0f else 2.5f}
    })

    LockBackground(wallpaper, viewModel::initOffsetY, viewModel::updateOffsetY) {
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp)
                    .aspectRatio(18f),
                tint = Color.White
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 150.dp)
            ) {
                DateTimeComposable()
            }
            Button(
                onClick = {
                    viewModel.unlock()
                }, modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .align(Alignment.Center)
            ) {
                Text(text = "UNLOCK")
            }
            Text(
                text = "Faites glisser pour déverrouiller",
                fontSize = adaptativeFontSize.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 35.dp),
                color = Color.White
            )
        }

    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LockBackground(wallpaper: ImageBitmap?, initOffset: (Float) -> Unit, updateOffset: (Float) -> Unit, content: @Composable () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black, shape = RectangleShape)
            .pointerInteropFilter {
                when(it.action) {
                    MotionEvent.ACTION_DOWN -> initOffset(it.y)
                    MotionEvent.ACTION_MOVE -> updateOffset(it.y)
                    MotionEvent.ACTION_UP -> initOffset(0f)
                }
                true
            }
//            .draggable(
//                orientation = Orientation.Vertical,
//                state = rememberDraggableState { delta ->
//                    updateOffset(delta)
//                },
//                onDragStopped = { updateOffset(0f) },
//                reverseDirection = true
//            )
    ) {
        if (wallpaper != null) {
            Image(
                painter = painterResource(id = R.drawable.wallpaper),//BitmapPainter(wallpaper),
                contentDescription = "Background",
                modifier = Modifier.fillMaxHeight(),
                contentScale = ContentScale.Crop
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
                text = "${dateTime.value.hour}:${dateTime.value.minute}",
                fontSize = 60.sp,
                color = Color.White
            )
            Text(
                text = "${dateTime.value.dayOfWeek} ${dateTime.value.dayOfMonth} ${dateTime.value.month}",
                fontSize = 25.sp,
                fontWeight = FontWeight.ExtraLight,
                color = Color.White
            )
        }

    }
}