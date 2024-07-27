package com.oyr.lockandr.lockscreen

import android.app.WallpaperManager
import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.oyr.lockandr.data.AppDatabase
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
class LockViewModel(private val lockAdmin: LockAdmin, private val database: AppDatabase) : ViewModel() {

    private val realCode = "1234"

    private val _inputCode = MutableStateFlow("")
    val inputCode = _inputCode.asStateFlow()

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

    fun updateDisplayedScreen(screen: DisplayedScreen) {
        _inputCode.update { "" }
        _displayedScreen.update { screen }
    }

    fun validateCode() {
        if (_inputCode.value == realCode) unlock()
    }

    fun updateInputCode(inputKey: String) =
        if (_inputCode.value.length < 6) _inputCode.update { it + inputKey } else {}

    fun deleteLast() = _inputCode.update { it.dropLast(1) }

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