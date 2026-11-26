
package com.example.healthmate.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(

    primary = Color(0xFF00796B),
    onPrimary = Color.White,

    secondary = Color(0xFF26A69A),
    onSecondary = Color(0xFF003731),

    tertiary = Color(0xFF4CAF50),

    background = Color(0xFFF0F5F4),
    onBackground = Color(0xFF263238),

    surface = Color.White,
    onSurface = Color(0xFF263238),

    surfaceVariant = Color(0xFFE0ECE9),
    onSurfaceVariant = Color(0xFF546E7A),

    outline = Color(0xFF90A4AE),

    error = Color(0xFFD32F2F),
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(

    primary = Color(0xFF80CBC4),
    onPrimary = Color(0xFF003731),

    secondary = Color(0xFF4DB6AC),
    onSecondary = Color(0xFF003731),

    tertiary = Color(0xFF81C784),

    background = Color(0xFF101C19),
    onBackground = Color(0xFFE0F2F1),

    surface = Color(0xFF20332E),
    onSurface = Color(0xFFE0F2F1),

    surfaceVariant = Color(0xFF29433D),
    onSurfaceVariant = Color(0xFFB0BEC5),

    outline = Color(0xFF78958F),

    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun HealthMateTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
