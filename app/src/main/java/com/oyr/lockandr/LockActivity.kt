package com.oyr.lockandr

import android.app.KeyguardManager
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
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
import com.oyr.lockandr.receivers.DevAdminReceiver

@Suppress("DEPRECATION")
class LockActivity : ComponentActivity(), LockAdmin {

    private val onBackPressedCallback by lazy {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // DO NOTHING
            }
        }
    }

    private val adminComponentName: ComponentName by lazy {
        ComponentName(this, DevAdminReceiver::class.java)
    }

    private val devicePolicyManager: DevicePolicyManager by lazy {
        getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    private val lockViewModel = LockViewModel(this)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        setContent {
        LockScreen(lockViewModel)
        }

    }

    override fun onStart() {
        super.onStart()
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }

    override fun unlock() {
        finish()
    }
}