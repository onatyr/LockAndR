package com.oyr.lockandr.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

//    @Provides
//    fun provideLockViewModel(lockService: LockService): LockViewModel {
//        return LockViewModel(lockService)
//    }
}