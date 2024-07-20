package com.oyr.lockandr.lockscreen

import android.app.WallpaperManager
import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

interface LockAdmin {
    fun lock()
    fun unlock()
}
class LockViewModel(private val lockAdmin: LockAdmin): ViewModel() {

    private var startCoordinate = 0f
    private val _offsetY = MutableStateFlow(0f)
    val offsetY = _offsetY.asStateFlow()

    fun lock() {
        lockAdmin.lock()
    }
    fun unlock() {
        lockAdmin.unlock()
    }

    fun initOffsetY(startCoordinate: Float) {
        this.startCoordinate = startCoordinate
        updateOffsetY(startCoordinate)
    }

    fun updateOffsetY(actualCoordinate: Float) {
        val newOffset = -(actualCoordinate - startCoordinate)
        if (newOffset in (0f..500f))
            _offsetY.value = newOffset
        else if (newOffset > 500f) {
//            unlock()
        }
    }

    fun getDeviceWallpaper(context: Context): ImageBitmap? {
        val wallpaperManager = WallpaperManager.getInstance(context)
        val drawable = wallpaperManager.fastDrawable
        if (drawable != null) {
            return drawable.toBitmap().asImageBitmap()
        }
        return null
    }
}