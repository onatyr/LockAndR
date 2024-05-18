package com.oyr.lockandr

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
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
import com.oyr.lockandr.ui.theme.LockAndRTheme


class MainActivity : ComponentActivity() {

    private lateinit var adminComponentName: ComponentName

    private val devicePolicyManager: DevicePolicyManager by lazy {
        getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    private val RESULT_ENABLE = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pm = packageManager
        var isAdminActive = false

        adminComponentName = ComponentName(this, DevAdminReceiver::class.java)
        try {
            isAdminActive = devicePolicyManager.isAdminActive(adminComponentName)
        } catch (e: Exception) {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponentName)
            intent.putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Additional text explaining why we need this permission"
            )
            startActivityForResult(intent, RESULT_ENABLE)
        }


        val appInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0L))
        } else {
            pm.getInstalledApplications(0)
        }

//        val makeHidden = !devicePolicyManager.isApplicationHidden(
//            adminComponentName,
//            "fr.onat68.aileronsappmapandroid"
//        )
//        devicePolicyManager.setApplicationHidden(
//            adminComponentName,
//            "fr.onat68.aileronsappmapandroid",
//            makeHidden
//        )
        enableEdgeToEdge()
        setContent {
            LockAndRTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        Spacer(modifier = Modifier.size(30.dp))
                        if (!isAdminActive) Button(onClick = {
                            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
                            intent.putExtra(
                                DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                                adminComponentName
                            )
                            intent.putExtra(
                                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                "Additional text explaining why we need this permission"
                            )
                            startActivityForResult(intent, RESULT_ENABLE)
                        }) {

                        }
                        LazyColumn {
                            items(appInfos) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp)
                                ) {
                                    Image(
                                        bitmap = drawableToBitmap(it.loadIcon(pm)).asImageBitmap(),
                                        contentDescription = "app icon",
                                        modifier = Modifier.size(50.dp)
                                    )
                                    Text(text = it.loadLabel(pm).toString())
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RESULT_ENABLE -> if (resultCode == RESULT_OK) {
                Toast.makeText(
                    this@MainActivity,
                    "You have enabled the Admin Device features",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "Problem to enable the Admin Device features",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

fun drawableToBitmap(drawable: Drawable): Bitmap {
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

