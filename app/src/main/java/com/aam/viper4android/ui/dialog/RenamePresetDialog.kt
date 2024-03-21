package com.aam.viper4android

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun RenamePresetDialog(
    name: String,
    onNameChanged: (String) -> Unit,
    onDismissRequest: () -> Unit,
) {
    var tmpName by remember(name) { mutableStateOf(name) }

    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = "Rename preset") },
        text = {
            Column {
                TextField(
                    value = tmpName,
                    onValueChange = { tmpName = it },
                    label = {
                        Text("Preset name")
                    }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onNameChanged(tmpName)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text("Cancel")
            }
        }
    )
}