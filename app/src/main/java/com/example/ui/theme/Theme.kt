package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = TelebirrGreenPrimary,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1B1D23),
    onPrimaryContainer = Color.White,
    secondary = TelebirrGoldAccent,
    onSecondary = Color.Black,
    secondaryContainer = TelebirrDarkCard,
    onSecondaryContainer = TelebirrGoldLight,
    tertiary = TelebirrGreenSecondary,
    background = TelebirrDarkBg,
    onBackground = Color(0xFFE2E2E6),
    surface = TelebirrDarkSurface,
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = TelebirrDarkCard,
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = TelebirrDarkBorder
)

private val LightColorScheme = darkColorScheme(
    primary = TelebirrGreenPrimary,
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1B1D23),
    onPrimaryContainer = Color.White,
    secondary = TelebirrGoldAccent,
    onSecondary = Color.Black,
    secondaryContainer = TelebirrDarkCard,
    onSecondaryContainer = TelebirrGoldLight,
    tertiary = TelebirrGreenSecondary,
    background = TelebirrDarkBg,
    onBackground = Color(0xFFE2E2E6),
    surface = TelebirrDarkSurface,
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = TelebirrDarkCard,
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = TelebirrDarkBorder
)

@Composable
fun TelebirrTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
