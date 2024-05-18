package com.oyr.lockandr.packagesscreen

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModel
import com.oyr.lockandr.DevAdminManager


class PackagesViewModel(val devAdminManager: DevAdminManager) : ViewModel() {

    val appInfos = devAdminManager.getAllPackages()

    fun getAppIcon(appInfo: ApplicationInfo) =
        devAdminManager.getAppIcon(appInfo)

    fun getAppLabel(appInfo: ApplicationInfo) =
        devAdminManager.getAppLabel(appInfo)

}