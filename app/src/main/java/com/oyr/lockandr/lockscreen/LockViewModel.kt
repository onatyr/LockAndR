package com.oyr.lockandr.lockscreen

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.oyr.lockandr.utils.drawableToBitmap

interface LockAdmin {
    fun unlock()
}
class LockViewModel(private val lockAdmin: LockAdmin): ViewModel() {

    fun unlock() {
        lockAdmin.unlock()
    }

    fun getDeviceWallpaper(context: Context): ImageBitmap? {
        val wallpaperManager = WallpaperManager.getInstance(context)
        val drawable = wallpaperManager.drawable
        if (drawable != null) {
            Log.d("debugwallp", "noooot null")
            return drawable.toBitmap().asImageBitmap()
//            return drawableToBitmap(drawable).asImageBitmap()
        }
        Log.d("debugwallp", "null")
        return null
    }
}