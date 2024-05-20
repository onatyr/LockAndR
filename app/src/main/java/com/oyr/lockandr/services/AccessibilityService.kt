package com.oyr.lockandr.services

import android.R
import android.accessibilityservice.AccessibilityService
import android.graphics.PixelFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi


class AccessibilityControlService : AccessibilityService() {

    lateinit var windowManager: WindowManager



    override fun onServiceConnected() {
        val display = getSystemService(WINDOW_SERVICE) as WindowManager
        val appOverlay: View = LayoutInflater.from(this).inflate(R.layout.app_overlay, null, false)
        val appOverlayLayoutParams = WindowManager.LayoutParams()
        appOverlayLayoutParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
        appOverlayLayoutParams.format = PixelFormat.TRANSLUCENT;
        appOverlayLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        appOverlayLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        appOverlayLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        display.addView(appOverlay, appOverlayLayoutParams)
    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event!!.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            val eventText = event.text.toString()
            if (eventText.contains("lock screen")) {
                showOverlay()
            }
        }
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }

    fun showOverlay() {

    }
}