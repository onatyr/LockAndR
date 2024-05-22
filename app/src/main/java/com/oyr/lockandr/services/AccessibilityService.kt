package com.oyr.lockandr.services

import com.oyr.lockandr.R
import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import com.oyr.lockandr.LockActivity
import com.oyr.lockandr.lockscreen.LockAdmin
import com.oyr.lockandr.lockscreen.LockScreen
import com.oyr.lockandr.lockscreen.LockViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner


class AccessibilityControlService : AccessibilityService(), LockAdmin {

    val windowManager get() = getSystemService(WINDOW_SERVICE) as WindowManager
    lateinit var composeView: ComposeView
    private var isOverlayDisplayed = false

    override fun onServiceConnected() {
        super.onServiceConnected()
        Log.e("debugservice", "CONNECTED")

    }

    override fun onCreate() {
        super.onCreate()
        Log.e("debugservice", "CREATED")
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        event ?: return
        Log.e("debugservice", "EVENT ${event.eventType}")
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Log.e("debugservice", "EVENT")
            showOverlay()
            composeView?.tag = "locked"
        }
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    fun showOverlay() {
        if (isOverlayDisplayed) return
        val layoutFlag: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        composeView = ComposeView(this)
        composeView?.setContent {
            LockScreen(viewModel = LockViewModel(this))
        }

        val viewModelStore = ViewModelStore()
        val viewModelStoreOwner = object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore
                get() = viewModelStore
        }
        val lifecycleOwner = MyLifecycleOwner()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        composeView?.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView?.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        composeView?.setViewTreeViewModelStoreOwner(viewModelStoreOwner)
        windowManager.addView(composeView!!, params)
        lifecycleOwner.setCurrentState(Lifecycle.State.CREATED)
        isOverlayDisplayed = true
    }

    override fun unlock() {
        Log.e("debugservice", "UNLOCKED")
        windowManager.removeView(composeView)
        isOverlayDisplayed = false
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("debugservice", "DESTROYED")
    }
}