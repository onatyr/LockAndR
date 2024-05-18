package com.oyr.lockandr

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build

interface AdminActivity {
    fun getPackageManager(): PackageManager
    fun startActivityForResult(intent: Intent, requestCode: Int)
}

class DevAdminManager(
    private val devicePolicyManager: DevicePolicyManager,
    private val adminComponentName: ComponentName,
    private val adminActivity: AdminActivity
) {

    companion object {
        const val RESULT_ENABLE = 11
    }

    val pm: PackageManager by lazy {
        adminActivity.getPackageManager()
    }

    fun isAdminActive(): Boolean {
        return devicePolicyManager.isAdminActive(adminComponentName)
    }

    fun requireAdminPermissions() {
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponentName)
        intent.putExtra(
            DevicePolicyManager.EXTRA_ADD_EXPLANATION,
            "The app require admin privilege to lock the screen"
        )
        adminActivity.startActivityForResult(intent, RESULT_ENABLE)
    }

    fun getAllPackages(): MutableList<ApplicationInfo> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0L))
        } else {
            pm.getInstalledApplications(0)
        }
    }

    fun getAppIcon(appInfo: ApplicationInfo) = appInfo.loadIcon(pm)

    fun getAppLabel(appInfo: ApplicationInfo) = appInfo.loadLabel(pm).toString()


    fun setHiddenStatus(packageName: String, status: Boolean) {
        devicePolicyManager.setApplicationHidden(
            adminComponentName,
            packageName,
            status
        )
    }
}