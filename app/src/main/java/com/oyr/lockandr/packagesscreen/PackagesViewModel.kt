package com.oyr.lockandr.packagesscreen

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import com.oyr.lockandr.DevAdminManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class AppInfo(val id: String, val label: String, val icon: Drawable)
class PackagesViewModel(val devAdminManager: DevAdminManager) : ViewModel() {

    private val appInfoListUnfiltered = devAdminManager.getAllPackages()
        .mapIndexed { index, appInfo ->
            AppInfo(
                id = UUID.randomUUID().toString(),
                label = devAdminManager.getAppLabel(appInfo),
                icon = devAdminManager.getAppIcon(appInfo)
            )
        }.sortedBy { it.label }

    private val _appInfoList = MutableStateFlow(appInfoListUnfiltered)
    val appInfoList = _appInfoList.asStateFlow()

    private val _searchAppQuery = MutableStateFlow("")
    val searchAppQuery = _searchAppQuery.asStateFlow()
    fun searchAppInfo(query: String) {
        _searchAppQuery.update { query }
        _appInfoList.update {
            appInfoListUnfiltered.filter { appInfo ->
                appInfo.label.contains(query, ignoreCase = true)
            }.toList()
        }
    }

}