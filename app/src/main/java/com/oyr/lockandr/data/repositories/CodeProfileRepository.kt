package com.oyr.lockandr.data.repositories

import com.oyr.lockandr.data.AppDatabase
import com.oyr.lockandr.data.entities.CodeProfile
import javax.inject.Inject

class CodeProfileRepository @Inject constructor(database: AppDatabase) {
    private val codeProfileDAO = database.codeProfileDao()

    fun getAll() = codeProfileDAO.getAll()

    suspend fun insert(codeProfile: CodeProfile) = codeProfileDAO.insert(codeProfile)

    suspend fun deleteById(id: Int) = codeProfileDAO.deleteById(id)
}