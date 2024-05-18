package com.oyr.lockandr.packagesscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.oyr.lockandr.drawableToBitmap

@Composable
fun PackagesScreen(viewModel: PackagesViewModel) {
    Column {
        Spacer(modifier = Modifier.size(30.dp))
        Button(onClick = { viewModel.devAdminManager.requireAdminPermissions() }) {}
        LazyColumn {
            items(viewModel.appInfos) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    Image(
                        bitmap = drawableToBitmap(it.loadIcon(viewModel.devAdminManager.pm)).asImageBitmap(),
                        contentDescription = "app icon",
                        modifier = Modifier.size(50.dp)
                    )
                    Text(text = viewModel.getAppLabel(it))
                }
            }
        }
    }
}