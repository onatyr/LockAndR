package com.oyr.lockandr

import android.graphics.PixelFormat
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.compose.ui.platform.ComposeView
import com.oyr.lockandr.lockscreen.LockAdmin
import com.oyr.lockandr.lockscreen.LockScreen
import com.oyr.lockandr.lockscreen.LockViewModel

@Suppress("DEPRECATION")
class LockActivity : ComponentActivity(), LockAdmin {

    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // DO NOTHING
            }
        }
    }

    private val lockViewModel = LockViewModel(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val display = getSystemService(WINDOW_SERVICE) as WindowManager
        val appOverlay: View = LayoutInflater.from(this).inflate(R.layout.app_overlay, null, false)
        val appOverlayLayoutParams = WindowManager.LayoutParams()
        appOverlayLayoutParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        appOverlayLayoutParams.format = PixelFormat.TRANSLUCENT;
        appOverlayLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        appOverlayLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        appOverlayLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        val composeView = appOverlay.findViewById<ComposeView>(R.id.compose_view)

        composeView.setContent {
            LockScreen(viewModel = lockViewModel)
        }
//        val viewModelStore = ViewModelStore()
//        val lifecycleOwner = MyLifecycleOwner()
//        lifecycleOwner.performRestore(null)
//        lifecycleOwner.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
//        composeView.setViewTreeLifecycleOwner(lifecycleOwner)
//        composeView.setViewTreeSavedStateRegistryOwner(lifecycleOwner)
//        composeView.setViewTreeViewModelStoreOwner(viewModelStore)
        display.addView(appOverlay, appOverlayLayoutParams)

    }

    override fun onStart() {
        super.onStart()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun lock() {
        TODO("Not yet implemented")
    }

    override fun unlock() {
        finish()
    }
}