package com.oyr.lockandr.presentation.packages

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import com.oyr.lockandr.DevAdminManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject

data class AppInfo(val id: String, val label: String, val icon: Drawable)

@HiltViewModel
class PackagesViewModel @Inject constructor(private val devAdminManager: DevAdminManager) :
    ViewModel() {

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

    fun requireAdminPermissions() = devAdminManager.requireAdminPermissions()
}