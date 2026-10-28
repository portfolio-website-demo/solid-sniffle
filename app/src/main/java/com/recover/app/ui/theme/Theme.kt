package com.recover.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val RecoverElectricBlue = Color(0xFF007BFF)
val RecoverDeepRoyalBlue = Color(0xFF0018FF)
val RecoverMagentaDot = Color(0xFFF500FF)
val RecoverBlack = Color(0xFF000000)
val RecoverCharcoalSurface = Color(0xFF141416)
val RecoverCardSurface = Color(0xFF1C1C1F)
val RecoverTextPrimary = Color(0xFFFFFFFF)
val RecoverTextSecondary = Color(0xFF9EA3AC)
val RecoverAccentBlue = Color(0xFF2979FF)

private val DarkColorPalette = darkColorScheme(
    primary = RecoverElectricBlue,
    secondary = RecoverAccentBlue,
    background = RecoverBlack,
    surface = RecoverCharcoalSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = RecoverTextPrimary,
    onSurface = RecoverTextPrimary
)

@Composable
fun RecoverTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorPalette,
        content = content
    )
}
