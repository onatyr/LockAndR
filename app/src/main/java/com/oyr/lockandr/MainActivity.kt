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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.oyr.lockandr.DevAdminManager.Companion.RESULT_ENABLE
import com.oyr.lockandr.packagesscreen.PackagesScreen
import com.oyr.lockandr.packagesscreen.PackagesViewModel
import com.oyr.lockandr.receivers.DevAdminReceiver
import com.oyr.lockandr.services.ScreenStateService
import com.oyr.lockandr.ui.theme.LockAndRTheme
import android.Manifest
import android.net.Uri
import android.provider.Settings
import android.util.Log


interface AdminActivity {
    fun getPackageManager(): PackageManager
    fun startActivityForResult(intent: Intent, requestCode: Int)
}

@Suppress("DEPRECATION")

class MainActivity : ComponentActivity(), AdminActivity {

    private val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    private val OVERLAY_PERMISSION_REQ_CODE = 2


    private val adminComponentName: ComponentName by lazy {
        ComponentName(this, DevAdminReceiver::class.java)
    }

    private val devicePolicyManager: DevicePolicyManager by lazy {
        getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    private lateinit var packagesViewModel: PackagesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val devAdminManager = DevAdminManager(devicePolicyManager, adminComponentName, this)
        packagesViewModel = PackagesViewModel(devAdminManager)

        val serviceIntent = Intent(this, ScreenStateService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            val intent =
                Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
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

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }
                return
            }

            else -> {
                // Ignore all other requests.
            }
        }
    }
}

