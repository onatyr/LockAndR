package com.oyr.lockandr.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.oyr.lockandr.LockActivity


class ScreenStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SCREEN_OFF -> {
//                val lockIntent = Intent(context, LockActivity::class.java)
//                lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
//                context.startActivity(lockIntent)
                Log.d("ScreenStateReceiver", "Screen turned off")
            }

            Intent.ACTION_SCREEN_ON -> {
                Log.d("ScreenStateReceiver", "Screen turned on")
            }
        }
    }
}
