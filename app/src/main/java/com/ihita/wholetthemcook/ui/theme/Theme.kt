package com.ihita.wholetthemcook.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = ButtonPrimary,
    secondary = ButtonSecondary,
    tertiary = IconPrimary,
    background = AppBackground,
    surface = LightLilac,
    onPrimary = TextBody,
    onSecondary = TextBody,
    onBackground = TextBody,
    onSurface = TextBody
)

private val LightColorScheme = lightColorScheme(
    primary = ButtonPrimary,
    secondary = ButtonSecondary,
    tertiary = IconPrimary,
    background = AppBackground,
    surface = LightLilac,
    onPrimary = TextBody,
    onSecondary = TextBody,
    onBackground = TextBody,
    onSurface = TextBody
)

@Composable
fun WhoLetThemCookTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}