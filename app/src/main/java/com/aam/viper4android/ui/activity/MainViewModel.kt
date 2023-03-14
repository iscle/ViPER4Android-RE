package com.aam.viper4android.ui.activity

import android.util.Log
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aam.viper4android.ViPERManager
import com.aam.viper4android.ui.effect.MasterLimiterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    private val TAG = "MainViewModel"

    val masterLimiterState = MasterLimiterState()

    init {
        observeMasterLimiter()
    }

    private fun observeMasterLimiter() {
        snapshotFlow { masterLimiterState.outputGain }
            .onEach {
                Log.d(TAG, "observeMasterLimiter: outputGain: $it")
            }
            .launchIn(viewModelScope)

        snapshotFlow { masterLimiterState.outputPan }
            .onEach {
                Log.d(TAG, "observeMasterLimiter: outputPan: $it")
            }
            .launchIn(viewModelScope)

        snapshotFlow { masterLimiterState.thresholdLimit }
            .onEach {
                Log.d(TAG, "observeMasterLimiter: thresholdLimit: $it")
            }
            .launchIn(viewModelScope)
    }

}