package com.oyr.lockandr.presentation.codeprofiles

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AddProfileButton() {
    Button(onClick = { /*TODO*/ }) {

    }
}

@Composable
fun CodeProfileList(codeProfilesFlow: StateFlow<List<String>>) {
    val codeProfiles = codeProfilesFlow.collectAsState()
    LazyColumn {
        item(codeProfiles.value) {
//            PackageCard(label = , icon = )
        }
    }
}

@Composable
fun PackageCheckbox() {
    Row {
//        PackageCard(label = , icon = )
//        Checkbox(checked = , onCheckedChange = )
    }
}