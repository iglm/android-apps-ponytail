package com.igl.monitoreobrc.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color(0xFFEF5350),
    secondary = Color(0xFFFFB74D),
    surface = Color(0xFF1E1E1E),
    background = Color(0xFF121212)
)

@Composable
fun MonitoreoTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkColors, content = content)
}
