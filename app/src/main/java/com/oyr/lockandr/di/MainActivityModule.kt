package com.oyr.lockandr.di

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import com.oyr.lockandr.AdminActivity
import com.oyr.lockandr.DevAdminManager
import com.oyr.lockandr.receivers.DevAdminReceiver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object MainActivityModule {

    @Provides
    fun provideDevicePolicyManager(@ActivityContext context: Context): DevicePolicyManager {
        return context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    @Provides
    fun provideAdminComponentName(@ActivityContext context: Context): ComponentName {
        return ComponentName(context, DevAdminReceiver::class.java)
    }

    @Provides
    fun provideDevAdminManager(
        devicePolicyManager: DevicePolicyManager,
        adminComponentName: ComponentName,
        @ActivityContext adminContext: Context
    ): DevAdminManager {
        val adminActivity = adminContext as AdminActivity
        return DevAdminManager(devicePolicyManager, adminComponentName, adminActivity)
    }
}