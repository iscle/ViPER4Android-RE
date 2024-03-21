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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aam.viper4android.ViPERManager
import com.aam.viper4android.ViPERService
import com.aam.viper4android.ui.MainScreen
import com.aam.viper4android.ui.SettingsScreen
import com.aam.viper4android.ui.theme.ViPER4AndroidTheme
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViPER4AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val startDestination = remember { getStartDestination() }
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                    ) {
                        composable("main") {
                            MainScreen(
                                onNavigateToSettings = {
                                    navController.navigate("settings")
                                }
                            )
                        }
                        composable("settings") {
                            SettingsScreen(
                                onNavigateUp = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("viper_unavailable") {
                            Text("ViPER is not available.")
                        }
                    }
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

    private fun getStartDestination(): String {
        return when {
            !ViPERManager.isViperAvailable -> "viper_unavailable"
            false -> "onboarding" // todo
            else -> "main"
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
}
