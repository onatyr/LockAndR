package com.oyr.lockandr.presentation.lock

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.StateFlow

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