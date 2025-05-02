package com.krishansubudhiapps.smartnews.ui.theme

import android.app.Activity

import android.os.Build

import androidx.compose.foundation.isSystemInDarkTheme

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.darkColorScheme

import androidx.compose.material3.dynamicDarkColorScheme

import androidx.compose.material3.dynamicLightColorScheme

import androidx.compose.material3.lightColorScheme

import androidx.compose.runtime.Composable

import androidx.compose.runtime.SideEffect

import androidx.compose.ui.graphics.toArgb

import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.platform.LocalView

import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(

    primary = NewsDarkRed,

    secondary = NewsDarkGray,

    tertiary = NewsDarkGray, // Using dark gray for tertiary as well

    background = NewsDarkGray, // Dark gray background

    surface = NewsDarkGray, // Dark gray surface

    onPrimary = NewsWhite, // White text on red

    onSecondary = NewsWhite, // White text on dark gray

    onTertiary = NewsWhite, // White text on dark gray

    onBackground = NewsWhite, // White text on dark gray background

    onSurface = NewsWhite // White text on dark gray surface

)

private val LightColorScheme = lightColorScheme(

    primary = NewsRed,

    secondary = NewsGray,

    tertiary = NewsGray, // Using gray for tertiary as well

    background = NewsWhite, // White background

    surface = NewsWhite, // White surface

    onPrimary = NewsWhite, // White text on red

    onSecondary = NewsWhite, // White text on gray

    onTertiary = NewsWhite, // White text on gray

    onBackground = NewsGray, // Gray text on white background

    onSurface = NewsGray // Gray text on white surface

)

@Composable

fun SmartNewsTheme(

    darkTheme: Boolean = isSystemInDarkTheme(),

    // Dynamic color is available on Android 12+

    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }


    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}