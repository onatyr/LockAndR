package com.oyr.lockandr.presentation.codeprofiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyr.lockandr.data.AppDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class CodeProfilesViewModel @Inject constructor(database: AppDatabase) : ViewModel() {
    val codeProfiles = database.codeProfileDao().getAll()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList<String>())


}