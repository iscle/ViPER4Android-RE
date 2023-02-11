package com.aam.viper4android

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun StatusDialog(onDismissRequest: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            // Dismiss the dialog when the user clicks outside the dialog or on the back
            // button. If you want to disable that functionality, simply use an empty
            // onDismissRequest.
            onDismissRequest()
        },
        title = {
            Text(text = "Status") },
        text = {
            Column {
                Text(text = "Driver version: v1.0.0")
                Text(text = "Enabled: No")
                Text(text = "Audio format: Unknown")
                Text(text = "Processing: No")
                Text(text = "Sampling rate: 48000 Hz")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Close")
            }
        }
    )
}