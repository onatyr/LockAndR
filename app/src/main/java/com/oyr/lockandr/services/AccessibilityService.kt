package com.oyr.lockandr.services

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Build
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.oyr.lockandr.lockscreen.DisplayedScreen
import com.oyr.lockandr.lockscreen.LockAdmin
import com.oyr.lockandr.lockscreen.LockScreen
import com.oyr.lockandr.lockscreen.LockViewModel
import com.oyr.lockandr.receivers.ScreenStateReceiver


class AccessibilityControlService : AccessibilityService(), LockAdmin {

    private val windowManager get() = getSystemService(WINDOW_SERVICE) as WindowManager
    private lateinit var composeView: ComposeView
    private val lockViewModel = LockViewModel(this)

    private var isOverlayDisplayed = false

    private val screenStateReceiver = ScreenStateReceiver(this)

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onServiceConnected() {
        super.onServiceConnected()
        showOverlay()
    }
    override fun onCreate() {
        super.onCreate()
        registerScreenStateReceiver()
    }

    @RequiresApi(Build.VERSION_CODES.R)
    fun showOverlay() {
        if (isOverlayDisplayed) return

        composeView = ComposeView(this)
        composeView.setContent {
            LockScreen(viewModel = lockViewModel)
        }

        val viewModelStore = ViewModelStore()
        val viewModelStoreOwner = object : ViewModelStoreOwner {
            override val viewModelStore: ViewModelStore
                get() = viewModelStore
        }
        val lifecycleOwner = MyLifecycleOwner()
        lifecycleOwner.performRestore(null)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_START)
        composeView.setViewTreeLifecycleOwner(lifecycleOwner)
        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
        composeView.setViewTreeViewModelStoreOwner(viewModelStoreOwner)

        windowManager.addView(composeView, getWindowManagerParams())
        
            composeView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                )
//        composeView.setOnApplyWindowInsetsListener { v, insets ->
//            val controller = v.windowInsetsController
//            if (controller != null) {
//                controller.hide(WindowInsets.Type.statusBars())
//                controller.hide(WindowInsets.Type.navigationBars())
//
//            }
//            insets
//        }
        isOverlayDisplayed = true
    }

    private fun registerScreenStateReceiver() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
        }
        registerReceiver(screenStateReceiver, filter)
    }

    private fun unregisterScreenStateReceiver() {
        unregisterReceiver(screenStateReceiver)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun getWindowManagerParams(): WindowManager.LayoutParams {
        val layoutFlag: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            layoutFlag,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                    or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_FULLSCREEN
                    or WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS
//                    or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
//                    or WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
//                    or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            ,
            PixelFormat.TRANSPARENT,
        )
    }

    override fun lock() {
        showOverlay()
        lockViewModel.updateDisplayedScreen(DisplayedScreen.SAVE_SCREEN)
    }

    override fun unlock() {
        windowManager.removeView(composeView)
        isOverlayDisplayed = false
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterScreenStateReceiver()
    }

    override fun onInterrupt() {}
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
}