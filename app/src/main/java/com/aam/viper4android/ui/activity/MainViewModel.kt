package com.aam.viper4android.ui.activity

import androidx.lifecycle.ViewModel
import com.aam.viper4android.ViPERManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val viperManager: ViPERManager,
) : ViewModel() {
    private val TAG = "MainViewModel"

    val viperState = ViPERState()
}