package com.aam.viper4android.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aam.viper4android.MainViewModel
import com.aam.viper4android.PresetDialog
import com.aam.viper4android.StatusDialog
import com.aam.viper4android.ui.effect.*
import com.aam.viper4android.ui.theme.ViPER4AndroidTheme

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    @OptIn(ExperimentalMaterial3Api::class)
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
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScreen(mainViewModel: MainViewModel = viewModel()) {
        val effectState = mainViewModel.uiState.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()
        var openStatusDialog by remember { mutableStateOf(false) }
        var openPresetDialog by remember { mutableStateOf(false) }
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
                        FloatingActionButton(
                            onClick = { /* do something */ },
                            containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Filled.PowerSettingsNew, "Localized description")
                        }
                    }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
        ) {
            Box(modifier = Modifier.padding(it)) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(8.dp)
                ) {
                    val effectSpacer = 8.dp
                    MasterLimiterEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    PlaybackGainControlEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    FETCompressorEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    ViPERDDCEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    SpectrumExtensionEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    FIREqualizerEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    ConvolverEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    FieldSurroundEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    DifferentialSurroundEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    HeadphoneSurroundPlusEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    ReverberationEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    DynamicSystemEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    TubeSimulator6N1JEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    ViPERBassEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    ViPERClarityEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    AuditorySystemProtectionEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    AnalogXEffect()
                    Spacer(modifier = Modifier.height(effectSpacer))
                    SpeakerOptimizationEffect()
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

    private fun openSettingsActivity() {
        Intent(this, SettingsActivity::class.java).also {
            startActivity(it)
        }
    }
}

//@Composable
//fun Greeting(name: String) {
//    Text(text = "Hello $name!")
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    ViPER4AndroidTheme {
//        Greeting("Android")
//    }
//}