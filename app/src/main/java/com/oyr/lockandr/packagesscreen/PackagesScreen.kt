package com.oyr.lockandr.packagesscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.oyr.lockandr.utils.drawableToBitmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackagesScreen(viewModel: PackagesViewModel) {
    val searchAppQuery = viewModel.searchAppQuery.collectAsState()

    Column {
        Spacer(modifier = Modifier.size(30.dp))
        Button(onClick = { viewModel.devAdminManager.requireAdminPermissions() }) {}
        Spacer(modifier = Modifier.size(30.dp))

        TextField(
            value = searchAppQuery.value,
            onValueChange = { newQuery -> viewModel.searchAppInfo(newQuery) },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn {
            items(items = viewModel.appInfoList.value,
                key = { it.id }) {
                PackageCard(
                    label = it.label,
                    icon = drawableToBitmap(it.icon).asImageBitmap(),
                )
            }
        }
    }
}