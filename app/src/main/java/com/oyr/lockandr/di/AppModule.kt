package com.oyr.lockandr.di

import android.content.Context
import androidx.room.Room
import com.oyr.lockandr.data.AppDatabase
import com.oyr.lockandr.services.LockService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "LockAndRDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLockService() = LockService()
}