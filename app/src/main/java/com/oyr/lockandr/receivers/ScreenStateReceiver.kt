package com.oyr.lockandr.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
//import com.oyr.lockandr.LockActivity
import com.oyr.lockandr.lockscreen.LockAdmin


class ScreenStateReceiver(val lockAdmin: LockAdmin) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SCREEN_OFF -> {
                lockAdmin.lock()
                Log.d("ScreenStateReceiver", "Screen turned off")
            }

            Intent.ACTION_SCREEN_ON -> {
                Log.d("ScreenStateReceiver", "Screen turned on")
            }
        }
    }
}
