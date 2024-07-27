package com.oyr.lockandr.data.entities

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "code_profile"
)
data class CodeProfile (
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "code") val code: Long,
)

@Dao
interface CodeProfileDAO {

}