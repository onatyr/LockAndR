package com.oyr.lockandr.presentation.lock

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CodeScreen(displayedScreen: DisplayedScreen, viewModel: LockViewModel) {
    val inputCode = viewModel.inputCode.collectAsState()
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
                Spacer(modifier = Modifier.size(200.dp))
                Text(
                    text = inputCode.value.map { '•' }.joinToString(""),
                    letterSpacing = 10.sp,
                    fontSize = 60.sp,
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(40.dp))
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