package com.oyr.lockandr.lockscreen

import android.os.Build
import android.view.MotionEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oyr.lockandr.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime


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

@Composable
fun SaveScreen(displayedScreen: DisplayedScreen, offsetYStateFlow: StateFlow<Float>) {
    val offsetY = offsetYStateFlow.collectAsState()
    val adaptativeFontRatio = 1f + when (offsetY.value) {
        in 0f..500f -> offsetY.value / 714.2f
        else -> {
            if (offsetY.value <= 0) 0f else 2.5f
        }
    }

    val animatedColor by animateColorAsState(
        targetValue = lerp(Color.White, Color.Transparent, offsetY.value / 600f), label = ""
    )

    AnimatedVisibility(
        visible = displayedScreen == DisplayedScreen.SAVE_SCREEN,
        exit = fadeOut()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp)
                    .aspectRatio(18f),
                tint = animatedColor
            )
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 150.dp)
            ) {
                DateTimeComposable(animatedColor)
            }

            Text(
                text = "Faites glisser pour déverrouiller",
                fontSize = (13f * adaptativeFontRatio).sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 35.dp),
                color = animatedColor
            )
        }
    }
}

@Composable
fun CodeScreen(displayedScreen: DisplayedScreen, viewModel: LockViewModel) {
    val blurredCode = viewModel.blurredCode.collectAsState()
    AnimatedVisibility(
        visible = displayedScreen == DisplayedScreen.CODE_SCREEN,
        enter = fadeIn()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = blurredCode.value,
                    fontSize = 20.sp,
                    color = Color.White
                )
                DigitKeyboard(
                    modifier = Modifier,
                    onDigitClick = viewModel::updateInputCode,
                    onBackClick = viewModel::deleteLast,
                    onValidateClick = viewModel::validateCode
                )
            }
        }
    }
}

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
            onButtonClick()
            /* TODO VIBRATE */
        }) {
        buttonContent()
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