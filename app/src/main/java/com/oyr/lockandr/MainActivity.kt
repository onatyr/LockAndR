package com.oyr.lockandr

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.oyr.lockandr.DevAdminManager.Companion.RESULT_ENABLE
import com.oyr.lockandr.presentation.packages.PackagesScreen
import com.oyr.lockandr.presentation.packages.PackagesViewModel
import com.oyr.lockandr.ui.theme.LockAndRTheme
import dagger.hilt.android.AndroidEntryPoint


interface AdminActivity {
    fun getPackageManager(): PackageManager
    fun startActivityForResult(intent: Intent, requestCode: Int)
}

@Suppress("DEPRECATION")
@AndroidEntryPoint
class MainActivity : ComponentActivity(), AdminActivity {

    private val PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    private val OVERLAY_PERMISSION_REQ_CODE = 2
    private val PERMISSIONS_REQUEST_MANAGE_EXTERNAL_STORAGE = 3


//    private val adminComponentName: ComponentName by lazy {
//        ComponentName(this, DevAdminReceiver::class.java)
//    }
//
//    private val devicePolicyManager: DevicePolicyManager by lazy {
//        getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//    }

    private val packagesViewModel: PackagesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
//                CodeProfilesScreen(CodeProfilesViewModel())
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

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
            !Environment.isExternalStorageManager()
        ) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivityForResult(intent, PERMISSIONS_REQUEST_MANAGE_EXTERNAL_STORAGE)
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
                    Toast.makeText(
                        this,
                        "Permission granted to read your External storage",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        this,
                        "Permission denied to read your External storage",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }

            else -> {
            }
        }
    }
}

