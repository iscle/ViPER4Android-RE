package com.aam.viper4android.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp

@Composable
fun EqualizerSurface() {
    Canvas(modifier = Modifier.size(100.dp)) {
        Path()

        drawPath(path = Path(), color = Color(0xFF000000))
    }
}