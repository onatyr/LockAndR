package com.oyr.lockandr.presentation.packages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp

@Composable
fun PackageCard(label: String, icon: ImageBitmap) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 25.dp, vertical = 10.dp)
    ) {
        Image(
            bitmap = icon,
            contentDescription = "app icon",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(text = label, color = Color.White)
    }

}