package com.oyr.lockandr.data.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(
    tableName = "code_profile"
)
data class CodeProfile (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "code") val code: Long,
    @ColumnInfo(name = "packages_to_delete") val packagesToDelete: List<String>,
    @ColumnInfo(name = "packages_to_hide") val packagesToHide: List<String>,
)

@Dao
interface CodeProfileDAO {
    @Query("""
        SELECT * FROM code_profile
    """)
    fun getAll(): Flow<CodeProfile>
}