package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = CervannaTealPrimary,
    onPrimary = Color.White,
    primaryContainer = CervannaTealContainer,
    onPrimaryContainer = CervannaOnTealContainer,
    secondary = CervannaAmberSecondary,
    onSecondary = Color.White,
    secondaryContainer = CervannaAmberContainer,
    onSecondaryContainer = CervannaOnAmberContainer,
    tertiary = MpesaGreen,
    onTertiary = Color.White,
    tertiaryContainer = MpesaGreenContainer,
    onTertiaryContainer = MpesaGreenDark,
    background = CervannaBackground,
    onBackground = CervannaTextPrimary,
    surface = CervannaSurface,
    onSurface = CervannaTextPrimary,
    surfaceVariant = CervannaSurfaceVariant,
    onSurfaceVariant = CervannaTextSecondary,
    outline = CervannaBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = CervannaTealLight,
    onPrimary = Color(0xFF00382E),
    primaryContainer = CervannaTealDark,
    onPrimaryContainer = CervannaTealContainer,
    secondary = CervannaAmberSecondary,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF5B3000),
    onSecondaryContainer = CervannaAmberContainer,
    tertiary = Color(0xFF4ADE80),
    onTertiary = Color(0xFF003914),
    tertiaryContainer = Color(0xFF14532D),
    onTertiaryContainer = MpesaGreenContainer,
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
