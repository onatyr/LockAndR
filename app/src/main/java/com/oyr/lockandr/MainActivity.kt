package com.oyr.lockandr

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.oyr.lockandr.DevAdminManager.Companion.RESULT_ENABLE
import com.oyr.lockandr.packagesscreen.PackagesScreen
import com.oyr.lockandr.packagesscreen.PackagesViewModel
import com.oyr.lockandr.ui.theme.LockAndRTheme

interface AdminActivity {
    fun getPackageManager(): PackageManager
    fun startActivityForResult(intent: Intent, requestCode: Int)
}
class MainActivity : ComponentActivity(), AdminActivity {

    private val adminComponentName: ComponentName by lazy {
        ComponentName(this, DevAdminReceiver::class.java)
    }

    private val devicePolicyManager: DevicePolicyManager by lazy {
        getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    lateinit var packagesViewModel: PackagesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val devAdminManager = DevAdminManager(devicePolicyManager, adminComponentName, this)
        packagesViewModel = PackagesViewModel(devAdminManager)
        val pm = packageManager
        var isAdminActive = false


        isAdminActive = devAdminManager.isAdminActive()

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

