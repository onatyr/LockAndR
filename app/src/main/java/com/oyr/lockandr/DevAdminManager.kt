package com.oyr.lockandr

import android.app.admin.DevicePolicyManager
import android.content.ComponentName

class DevAdminManager(
    private val devicePolicyManager: DevicePolicyManager,
    private val adminComponentName: ComponentName
) {

    fun setHiddenStatus(packageName: String, status: Boolean) {
        devicePolicyManager.setApplicationHidden(
            adminComponentName,
            "fr.onat68.aileronsappmapandroid",
            status
        )
    }
}