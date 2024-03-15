package com.aam.viper4android.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.PresetDialog
import com.aam.viper4android.StatusDialog
import com.aam.viper4android.ui.effect.AnalogXEffect
import com.aam.viper4android.ui.effect.AuditorySystemProtectionEffect
import com.aam.viper4android.ui.effect.ConvolverEffect
import com.aam.viper4android.ui.effect.DifferentialSurroundEffect
import com.aam.viper4android.ui.effect.DynamicSystemEffect
import com.aam.viper4android.ui.effect.FETCompressorEffect
import com.aam.viper4android.ui.effect.FIREqualizerEffect
import com.aam.viper4android.ui.effect.FieldSurroundEffect
import com.aam.viper4android.ui.effect.HeadphoneSurroundPlusEffect
import com.aam.viper4android.ui.effect.MasterLimiterEffect
import com.aam.viper4android.ui.effect.PlaybackGainControlEffect
import com.aam.viper4android.ui.effect.ReverberationEffect
import com.aam.viper4android.ui.effect.SpeakerOptimizationEffect
import com.aam.viper4android.ui.effect.SpectrumExtensionEffect
import com.aam.viper4android.ui.effect.TubeSimulator6N1JEffect
import com.aam.viper4android.ui.effect.ViPERBassEffect
import com.aam.viper4android.ui.effect.ViPERClarityEffect
import com.aam.viper4android.ui.effect.ViPERDDCEffect
import com.aam.viper4android.vm.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToSettings: () -> Unit
) {
    val enabled = viewModel.enabled.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    var openStatusDialog by rememberSaveable { mutableStateOf(false) }
    var openPresetDialog by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "ViPER4Android",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Settings"
                        )
                    }
                    IconButton(onClick = { openStatusDialog = true }) {
                        Icon(
                            Icons.Filled.Memory,
                            contentDescription = "Status",
                        )
                    }
                    IconButton(onClick = { openPresetDialog = true }) {
                        Icon(
                            Icons.Filled.Bookmark,
                            contentDescription = "Presets",
                        )
                    }
                },
                floatingActionButton = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        FloatingActionButton(
                            onClick = { viewModel.setEnabled(!enabled) },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Filled.PowerSettingsNew, "Localized description")
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) {
        Box(modifier = Modifier.padding(it)) {
            val scrollState = rememberScrollState()
            if (enabled) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    MasterLimiterEffect()
                    PlaybackGainControlEffect()
                    FETCompressorEffect()
                    ViPERDDCEffect()
                    SpectrumExtensionEffect()
                    FIREqualizerEffect()
                    ConvolverEffect()
                    FieldSurroundEffect()
                    DifferentialSurroundEffect()
                    HeadphoneSurroundPlusEffect()
                    ReverberationEffect()
                    DynamicSystemEffect()
                    TubeSimulator6N1JEffect()
                    ViPERBassEffect()
                    ViPERClarityEffect()
                    AuditorySystemProtectionEffect()
                    AnalogXEffect()
                    SpeakerOptimizationEffect()
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("ViPER is disabled.")
                }
            }
            if (openStatusDialog) {
                StatusDialog(onDismissRequest = { openStatusDialog = false })
            }
            if (openPresetDialog) {
                PresetDialog(onDismissRequest = { openPresetDialog = false })
            }
        }
    }
}