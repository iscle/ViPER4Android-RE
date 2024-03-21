package com.aam.viper4android.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// A utility function to debounce any function call
fun debounce(
    delayMillis: Long = 300L, // Default delay of 300ms
    scope: CoroutineScope, // CoroutineScope in which to launch the debounce
    action: () -> Unit // The action to debounce
): () -> Unit {
    var debounceJob: Job? = null
    return {
        debounceJob?.cancel() // Cancel the previous job if it's still active
        debounceJob = scope.launch {
            delay(delayMillis) // Wait for the delay
            action() // Execute the action
        }
    }
}
