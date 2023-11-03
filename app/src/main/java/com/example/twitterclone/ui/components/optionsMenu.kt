package com.example.twitterclone.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun ThreeDotOptionsMenu(
    onDeleteClick: () -> Unit
) {
    val expandedState = remember{
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
    ) {
        IconToggleButton(
            checked = expandedState.value,
            onCheckedChange = { expanded ->
                expandedState.value = expanded
            },
            content = {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options Menu")
            }
            , modifier = Modifier.height(20.dp)
        )

        DropdownMenu(
            expanded = expandedState.value,
            onDismissRequest = {
                expandedState.value = false
            }
        ) {
            DropdownMenuItem(
               text = {
                   Text(text = "Delete")
               },
                onClick = {
                    onDeleteClick()
                }
            )
        }
    }
}
