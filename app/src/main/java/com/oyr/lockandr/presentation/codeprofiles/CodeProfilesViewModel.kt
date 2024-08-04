package com.oyr.lockandr.presentation.codeprofiles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oyr.lockandr.data.AppDatabase
import com.oyr.lockandr.data.entities.CodeProfile
import com.oyr.lockandr.data.repositories.CodeProfileRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class CodeProfilesViewModel @Inject constructor(database: AppDatabase) : ViewModel() {
    val codeProfileRepository = CodeProfileRepository(database)

    val codeProfiles = database.codeProfileDao().getAll()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList<String>())

    fun addCodeProfile(codeProfile: CodeProfile) {
        viewModelScope.launch {
            codeProfileRepository.insert(codeProfile)
        }
    }

    fun deleteCodeProfile(codeProfile: CodeProfile) {
        viewModelScope.launch {
            codeProfileRepository.deleteById(codeProfile.id)
        }
    }
}