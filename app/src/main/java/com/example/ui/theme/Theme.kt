package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TvBlue,
    onPrimary = Color.White,
    primaryContainer = TvDarkSurface,
    onPrimaryContainer = Color.White,
    secondary = TvGreen,
    onSecondary = Color.White,
    background = TvDark,
    onBackground = Color.White,
    surface = TvDarkSurface,
    onSurface = Color.White,
    surfaceVariant = TvDarkCard,
    onSurfaceVariant = TvGray,
    outline = TvDarkBorder,
    error = TvRed
)

private val LightColorScheme = lightColorScheme(
    primary = TvBlue,
    onPrimary = Color.White,
    primaryContainer = TvLightBlue,
    onPrimaryContainer = TvBlue,
    secondary = TvGreen,
    onSecondary = Color.White,
    background = Color.White,
    onBackground = TvDark,
    surface = Color.White,
    onSurface = TvDark,
    surfaceVariant = TvBgLight,
    onSurfaceVariant = TvGray,
    outline = TvBorder,
    error = TvRed
)

@Composable
fun TradingViewTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
