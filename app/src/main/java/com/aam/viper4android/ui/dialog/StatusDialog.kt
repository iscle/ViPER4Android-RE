package com.aam.viper4android

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.aam.viper4android.util.AndroidUtils

@Composable
fun StatusDialog(viperManager: ViPERManager, onDismissRequest: () -> Unit) {
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
            val context = LocalContext.current
            val scrollState = rememberScrollState()
            Column(Modifier.verticalScroll(scrollState)) {
                val activeSessions = viperManager.currentSessions.collectAsState().value
                if (activeSessions.isEmpty()) {
                    Text(text = "No active sessions")
                } else {
                    activeSessions.forEachIndexed { index, activeSession ->
                        Row {
                            Column {
                                if (index != 0) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Divider()
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                val label = AndroidUtils.getApplicationLabel(context, activeSession.packageName)
                                Text(text = label, overflow = TextOverflow.Ellipsis, maxLines = 1, style = MaterialTheme.typography.labelLarge)
                                Text(text = "Driver version: v1.0.0")
                                Text(text = "Enabled: No")
                                Text(text = "Audio format: Unknown")
                                Text(text = "Processing: No")
                                Text(text = "Sampling rate: 48000 Hz")
                            }
                        }
                    }
                }
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