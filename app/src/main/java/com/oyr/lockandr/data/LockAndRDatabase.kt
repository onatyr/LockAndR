package com.oyr.lockandr.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.oyr.lockandr.data.entities.CodeProfile
import com.oyr.lockandr.data.entities.CodeProfileDAO

@Database(entities = [CodeProfile::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun codeProfileDao(): CodeProfileDAO
}