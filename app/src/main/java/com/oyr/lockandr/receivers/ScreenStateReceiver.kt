package com.oyr.lockandr.receivers

import android.app.KeyguardManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.oyr.lockandr.LockActivity


class ScreenStateReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SCREEN_OFF -> {
                val lockIntent = Intent(context, LockActivity::class.java)
                lockIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(lockIntent)
                Log.d("ScreenStateReceiver","Screen turned off")
            }
            Intent.ACTION_SCREEN_ON -> {
                val lockIntent = Intent(context, LockActivity::class.java)
                lockIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(lockIntent)
                Log.d("ScreenStateReceiver", "Screen turned on")
            }
        }
    }
}