package com.aam.viper4android.ui.effect

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.aam.viper4android.EffectCard
import com.aam.viper4android.R
import com.aam.viper4android.ui.ValueSlider
import com.aam.viper4android.ui.component.ValuePicker
import com.aam.viper4android.vm.DynamicSystemViewModel

@Composable
fun DynamicSystemEffect(
    viewModel: DynamicSystemViewModel = hiltViewModel()
) {
    val enabled = viewModel.enabled.collectAsState().value
    val deviceType = viewModel.deviceType.collectAsState().value
    val dynamicBassStrength = viewModel.dynamicBassStrength.collectAsState().value

    EffectCard(icon = painterResource(R.drawable.ic_dynamic_system),
        name = "Dynamic system",
        enabled = enabled,
        onEnabledChange = viewModel::setEnabled
    ) {
        Column {
            ValuePicker(
                title = "Device type",
                values = arrayOf(
                    "Extreme headphone(v2)",
                    "High-end headphone(v2)",
                    "Common headphone(v2)",
                    "Low-end headphone(v2)",
                    "Common earphone(v2)",
                    "Extreme headphone(v1)",
                    "High-end headphone(v1)",
                    "Common headphone(v1)",
                    "Common earphone(v1)"
                ),
                selectedIndex = deviceType,
                onSelectedIndexChange = viewModel::setDeviceType
            )
            ValueSlider(
                title = "Dynamic bass strength",
                summaryUnit = "%",
                value = dynamicBassStrength,
                onValueChange = viewModel::setDynamicBassStrength,
                valueRange = 0..100
            )
        }
    }
}