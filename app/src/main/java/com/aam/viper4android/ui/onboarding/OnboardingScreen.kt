package com.aam.viper4android.ui.onboarding

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.BuildConfig
import com.aam.viper4android.R
import com.aam.viper4android.driver.ViPEREffect
import com.aam.viper4android.ui.component.OnboardingCard

@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit,
    onboardingViewModel: OnboardingViewModel = hiltViewModel(),
) {
    val context = LocalContext.current

    var isIgnoringBatteryOptimizations by remember { mutableStateOf(onboardingViewModel.isIgnoringBatteryOptimizations()) }
    var hasNotificationPermission by remember { mutableStateOf(onboardingViewModel.hasNotificationPermission()) }

    val batteryOptimizationsResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        isIgnoringBatteryOptimizations = onboardingViewModel.isIgnoringBatteryOptimizations()
    }

    val notificationPermissionResultLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        hasNotificationPermission = it
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    top = 48.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_viper),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = stringResource(R.string.onboarding_title),
                fontSize = 45.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 45.sp
            )

            Text(
                text = stringResource(R.string.onboarding_subtitle),
                style = MaterialTheme.typography.bodyLarge,
            )

            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OnboardingCard(
                    isDone = ViPEREffect.isAvailable,
                    title = stringResource(R.string.onboarding_driver_title),
                    description = stringResource(R.string.onboarding_driver_description),
                    onClick = {  }
                )

                OnboardingCard(
                    isDone = isIgnoringBatteryOptimizations,
                    title = stringResource(R.string.onboarding_battery_optimization_title),
                    description = stringResource(R.string.onboarding_battery_optimization_subtitle),
                    onClick = {
                        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                            .setData("package:${context.packageName}".toUri())
                        batteryOptimizationsResultLauncher.launch(intent)
                    }
                )

                OnboardingCard(
                    isDone = hasNotificationPermission,
                    title = stringResource(R.string.onboarding_notification_title),
                    description = stringResource(R.string.onboarding_notification_subtitle),
                    onClick = { if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) notificationPermissionResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }
                )

                Spacer(Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Button(
                        onClick = onOnboardingComplete,
                        enabled = (ViPEREffect.isAvailable || BuildConfig.DEBUG) && isIgnoringBatteryOptimizations && hasNotificationPermission
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowForward,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(text = stringResource(R.string.onboarding_finish))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    Surface {
        OnboardingScreen(
            onOnboardingComplete = {}
        )
    }
}