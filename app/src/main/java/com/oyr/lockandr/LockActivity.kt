package com.oyr.lockandr

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
        }

        setContent {
        LockScreen(lockViewModel)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
    }

    override fun onStart() {
        super.onStart()
        startLockTask()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun unlock() {
        stopLockTask()
        finish()
    }
}