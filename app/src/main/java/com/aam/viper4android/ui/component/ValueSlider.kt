package com.aam.viper4android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import kotlin.math.roundToInt

@Composable
fun ValueSlider(
    title: String,
    summary: String? = null,
    summaryUnit: String = "",
    value: Int,
    onValueChange: (Int) -> Unit,
    valueRange: ClosedRange<Int> = 0..1,
    steps: Int = 0
) {
    Column {
        Text(text = title)
        Text(text = (summary ?: value.toString()) + summaryUnit)
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.roundToInt()) },
            valueRange = valueRange.let { it.start.toFloat()..it.endInclusive.toFloat() },
            steps = steps
        )
    }
}

@Composable
fun ValueSlider(
    title: String,
    summary: String? = null,
    summaryUnit: String = "",
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0
) {
    Column {
        Text(text = title)
        Text(text = (summary ?: value.toString()) + summaryUnit)
        Slider(value = value, onValueChange = onValueChange, valueRange = valueRange, steps = steps)
    }
}