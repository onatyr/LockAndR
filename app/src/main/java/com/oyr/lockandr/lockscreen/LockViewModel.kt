package com.oyr.lockandr.lockscreen

import android.app.WallpaperManager
import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel

interface LockAdmin {
    fun lock()
    fun unlock()
}
class LockViewModel(private val lockAdmin: LockAdmin): ViewModel() {

    fun lock() {
        lockAdmin.lock()
    }
    fun unlock() {
        lockAdmin.unlock()
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