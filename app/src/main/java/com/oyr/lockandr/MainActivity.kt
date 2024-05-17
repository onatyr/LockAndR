package com.oyr.lockandr

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.oyr.lockandr.ui.theme.LockAndRTheme


class MainActivity : ComponentActivity() {

    private val adminComponentName: ComponentName by lazy {
        ComponentName(this, DevAdminReceiver::class.java)
    }

    private val devicePolicyManager: DevicePolicyManager by lazy {
        getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pm = packageManager

        val appInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pm.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(0L)).filter { it.isProfileable }
        } else {
            pm.getInstalledApplications(0)
        }

        val makeHidden = !devicePolicyManager.isApplicationHidden(
            adminComponentName,
            "fr.onat68.aileronsappmapandroid"
        )
        devicePolicyManager.setApplicationHidden(
            adminComponentName,
            "fr.onat68.aileronsappmapandroid",
            makeHidden
        )
        enableEdgeToEdge()
        setContent {
            LockAndRTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
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

