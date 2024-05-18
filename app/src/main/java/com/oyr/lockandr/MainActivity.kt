package com.oyr.lockandr

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.oyr.lockandr.DevAdminManager.Companion.RESULT_ENABLE
import com.oyr.lockandr.packagesscreen.PackagesScreen
import com.oyr.lockandr.packagesscreen.PackagesViewModel
import com.oyr.lockandr.receivers.DevAdminReceiver
import com.oyr.lockandr.services.ScreenStateService
import com.oyr.lockandr.ui.theme.LockAndRTheme

interface AdminActivity {
    fun getPackageManager(): PackageManager
    fun startActivityForResult(intent: Intent, requestCode: Int)
}
@Suppress("DEPRECATION")
class MainActivity : ComponentActivity(), AdminActivity {

    private val adminComponentName: ComponentName by lazy {
        ComponentName(this, DevAdminReceiver::class.java)
    }

    private val devicePolicyManager: DevicePolicyManager by lazy {
        getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    private lateinit var packagesViewModel: PackagesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        devicePolicyManager.setLockTaskPackages(adminComponentName,
            arrayOf("com.oyr.lockandr")
        )
        val devAdminManager = DevAdminManager(devicePolicyManager, adminComponentName, this)
        packagesViewModel = PackagesViewModel(devAdminManager)

        val serviceIntent = Intent(this, ScreenStateService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }


//        var isAdminActive = false
//        isAdminActive = devAdminManager.isAdminActive()

        enableEdgeToEdge()
        setContent {
            LockAndRTheme(darkTheme = true) {
                PackagesScreen(packagesViewModel)
            }
        }
    }

    override fun startActivityForResult(intent: Intent, requestCode: Int) {
        super.startActivityForResult(intent, requestCode)
    }

    override fun getPackageManager(): PackageManager = super.getPackageManager()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RESULT_ENABLE -> if (resultCode == RESULT_OK) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.admin_device_enabled,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    R.string.admin_device_activation_error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

