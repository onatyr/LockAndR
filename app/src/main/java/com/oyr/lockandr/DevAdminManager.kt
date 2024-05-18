package com.oyr.lockandr

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build

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
            R.string.admin_device_request_permissions
        )
        adminActivity.startActivityForResult(intent, RESULT_ENABLE)
    }

    fun getAllPackages(): List<ApplicationInfo> {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val resolvedInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.queryIntentActivities(
                mainIntent,
                PackageManager.ResolveInfoFlags.of(0L)
            )
        } else {
            pm.queryIntentActivities(mainIntent, 0)
        }

        val packageList = resolvedInfos.map { it.activityInfo.applicationInfo }
        return packageList
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