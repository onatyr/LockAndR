package com.oyr.lockandr.lockscreen

import android.app.WallpaperManager
import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class DisplayedScreen {
    SAVE_SCREEN,
    CODE_SCREEN
}

interface LockAdmin {
    fun lock()
    fun unlock()
}
class LockViewModel(private val lockAdmin: LockAdmin): ViewModel() {

    private val realCode = "1234"
    private var inputCode = ""

    private val _blurredCode = MutableStateFlow("")
    val blurredCode = _blurredCode.asStateFlow()

    private var startCoordinate = 0f
    private val _offsetY = MutableStateFlow(0f)
    val offsetY = _offsetY.asStateFlow()

    private val _displayedScreen = MutableStateFlow(DisplayedScreen.SAVE_SCREEN)
    val displayedBox = _displayedScreen.asStateFlow()

    private fun lock() {
        lockAdmin.lock()
    }
    private fun unlock() {
        lockAdmin.unlock()
    }

    fun initOffsetY(startCoordinate: Float) {
        this.startCoordinate = startCoordinate
        updateOffsetY(startCoordinate)
    }

    fun updateOffsetY(actualCoordinate: Float) {
        val newOffset = -(actualCoordinate - startCoordinate)
        if (newOffset in (0f..500f))
            _offsetY.update { newOffset }
        else if (newOffset > 600f) {
            updateDisplayedScreen(DisplayedScreen.CODE_SCREEN)
        }
    }

    private fun updateDisplayedScreen(screen: DisplayedScreen) = _displayedScreen.update { screen }

    fun validateCode() {
        if (inputCode == realCode) unlock()
    }

    fun updateInputCode(inputKey: String) {
        inputCode += inputKey
        _blurredCode.update { inputCode.map { '*' }.joinToString("") }
    }

    fun deleteLast() {
        inputCode = inputCode.dropLast(1)
        _blurredCode.update { inputCode.map { '*' }.joinToString("") }
    }

    // TODO Wallpaper is hard to work with (scale, crop ratio), better implement a feature where the user can add his own wallpaper
    fun getDeviceWallpaper(context: Context): ImageBitmap? {
        val wallpaperManager = WallpaperManager.getInstance(context)
        val drawable = wallpaperManager.fastDrawable
        if (drawable != null) {
            return drawable.toBitmap().asImageBitmap()
        }
        return null
    }
}