package com.aam.viper4android.ui.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import kotlin.math.roundToInt

private val MIN_DB = -12.0f
private val MAX_DB = 12.0f

private val text10Band = arrayOf(
    "31",
    "62",
    "125",
    "250",
    "500",
    "1k",
    "2k",
    "4k",
    "8k",
    "16k",
)

private val text15Band = arrayOf(
    "25",
    "40",
    "63",
    "100",
    "160",
    "250",
    "400",
    "630",
    "1k",
    "1.6k",
    "2.5k",
    "4k",
    "6.3k",
    "10k",
    "16k",
)

private val text25Band = arrayOf(
    "20",
    "31.5",
    "40",
    "50",
    "80",
    "100",
    "125",
    "160",
    "250",
    "315",
    "400",
    "500",
    "800",
    "1k",
    "1.25k",
    "1.6k",
    "2.5k",
    "3.15k",
    "4k",
    "5k",
    "8k",
    "10k",
    "12.5k",
    "16k",
    "20k",
)

private val text31Band = arrayOf(
    "20",
    "25",
    "31.5",
    "40",
    "50",
    "63",
    "80",
    "100",
    "125",
    "160",
    "200",
    "250",
    "315",
    "400",
    "500",
    "630",
    "800",
    "1k",
    "1.25k",
    "1.6k",
    "2k",
    "2.5k",
    "3.15k",
    "4k",
    "5k",
    "6.3k",
    "8k",
    "10k",
    "12.5k",
    "16k",
    "20k",
)

/**
 * A visualizer for the equalizer bands.
 *
 * @param bands The number of bands to display.
 */
@Composable
fun Equalizer(
    modifier: Modifier = Modifier,
    values: FloatArray,
) {
    val textMeasurer = rememberTextMeasurer()
    val bands = remember(values) { values.size }
    val bandText = remember(bands) {
        when (bands) {
            10 -> text10Band
            15 -> text15Band
            25 -> text25Band
            31 -> text31Band
            else -> throw IllegalArgumentException("Unsupported number of bands: $bands")
        }
    }

    // Define a vertical gradient
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(Color.Blue, Color.LightGray)
    )
    val horizontalLineColor = MaterialTheme.colorScheme.surfaceDim

    Canvas(modifier = modifier) {
        // band spacing is always calculated for 10 bands
        val bandSpacing = size.width / (10 + 1)

        fun dbY(db: Float): Float {
            return size.height * (1 - (db - MIN_DB) / (MAX_DB - MIN_DB))
        }
        
        // draw horizontal lines
        val lines = ((MAX_DB - MIN_DB) / 3.0f).roundToInt()
        for (i in 0 until lines) {
            val y = size.height * (i + 1) / (lines + 1)
            drawLine(
                color = horizontalLineColor,
                start = Offset(0f, y),
                end = Offset(size.width, y),
            )
        }

        // draw top and bottom text
        for (i in 0 until bands) {
            val x = bandSpacing * (i + 1)

            // top text
            val topTextLayoutResult = textMeasurer.measure(values[i].toString())
            val topTextTopLeft = Offset(x - topTextLayoutResult.size.width / 2, 0f)
            drawText(
                textLayoutResult = topTextLayoutResult,
                color = Color.White,
                topLeft = topTextTopLeft,
            )

            // bottom text
            val bottomTextLayoutResult = textMeasurer.measure(bandText[i])
            val bottomTextTopLeft = Offset(x - bottomTextLayoutResult.size.width / 2, size.height - bottomTextLayoutResult.size.height)
            drawText(
                textLayoutResult = bottomTextLayoutResult,
                color = Color.White,
                topLeft = bottomTextTopLeft,
            )
        }
    }
}

@Preview
@Composable
fun EqualizerPreview() {
    Equalizer(
        modifier = Modifier.fillMaxSize(),
        values = floatArrayOf(
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
            0.0f,
        )
    )
}