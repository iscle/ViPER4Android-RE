package com.aam.viper4android.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aam.viper4android.PresetDialog
import com.aam.viper4android.StatusDialog
import com.aam.viper4android.ViPERManager
import com.aam.viper4android.ViPERService
import com.aam.viper4android.ui.effect.*
import com.aam.viper4android.ui.theme.ViPER4AndroidTheme
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "MainActivity"

class ViPERState {
    var enabled by mutableStateOf(false)
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var viperManager: ViPERManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViPER4AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }

        requestNotificationPermissions()
        requestIgnoreBatteryOptimizations()
        Intent(this, ViPERService::class.java).let {
            try {
                ContextCompat.startForegroundService(this, it)
            } catch (e: Exception) {
                Log.e(TAG, "onCreate: Failed to start service", e)
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }
    }

    private fun requestNotificationPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) return

        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            Log.d(TAG, "requestNotificationPermissions() result called with: isGranted = $isGranted")
        }.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    // Required for creating foreground services with the app in the background
    @SuppressLint("BatteryLife")
    private fun requestIgnoreBatteryOptimizations() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (powerManager.isIgnoringBatteryOptimizations(packageName)) return

        registerForActivityResult(object : ActivityResultContract<Unit, Boolean>() {
            override fun createIntent(context: Context, input: Unit): Intent {
                return Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).also {
                    it.data = Uri.parse("package:$packageName")
                }
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
                return powerManager.isIgnoringBatteryOptimizations(packageName)
            }
        }) { isIgnoringBatteryOptimizations ->
            Log.d(TAG, "requestIgnoreBatteryOptimizations: Battery optimizations ignored: $isIgnoringBatteryOptimizations")
        }.launch(Unit)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(
        mainViewModel: MainViewModel = viewModel()
    ) {
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
                        IconButton(onClick = { openSettingsActivity() }) {
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
                                onClick = { mainViewModel.viperState.enabled = !mainViewModel.viperState.enabled },
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
                if (mainViewModel.viperState.enabled) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(scrollState)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        MasterLimiterEffect(state = mainViewModel.masterLimiterState)
                        PlaybackGainControlEffect()
                        FETCompressorEffect()
                        ViPERDDCEffect()
                        SpectrumExtensionEffect(state = mainViewModel.spectrumExtensionState)
                        FIREqualizerEffect(state = mainViewModel.firEqualizerState)
                        ConvolverEffect()
                        FieldSurroundEffect(state = mainViewModel.fieldSurroundState)
                        DifferentialSurroundEffect(state = mainViewModel.differentialSurroundState)
                        HeadphoneSurroundPlusEffect()
                        ReverberationEffect()
                        DynamicSystemEffect(state = mainViewModel.dynamicSystemState)
                        TubeSimulator6N1JEffect(state = mainViewModel.tubeSimulator6N1JState)
                        ViPERBassEffect(state = mainViewModel.viperBassState)
                        ViPERClarityEffect(state = mainViewModel.viperClarityState)
                        AuditorySystemProtectionEffect(state = mainViewModel.auditorySystemProtectionState)
                        AnalogXEffect(state = mainViewModel.analogXState)
                        SpeakerOptimizationEffect(state = mainViewModel.speakerOptimizationState)
                    }
                } else {
                    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("ViPER is disabled.")
                    }
                }
                if (openStatusDialog) {
                    StatusDialog(viperManager = viperManager, onDismissRequest = { openStatusDialog = false })
                }
                if (openPresetDialog) {
                    PresetDialog(onDismissRequest = { openPresetDialog = false })
                }
            }
        }
    }

    private fun openSettingsActivity() {
//        Intent(this, SettingsActivity::class.java).also {
//            startActivity(it)
//        }
    }
}
