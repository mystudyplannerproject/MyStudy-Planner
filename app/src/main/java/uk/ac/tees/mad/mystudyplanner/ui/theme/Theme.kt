package uk.ac.tees.mad.mystudyplanner.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkBluePrimary,
    secondary = DarkBlueSecondary,
    tertiary = DarkBlueTertiary,

    background = DarkBlueBackground,
    surface = DarkBlueSurface,

    onPrimary = DarkBlueOnPrimary,
    onSecondary = DarkBlueOnSecondary,
    onTertiary = DarkBlueOnTertiary,
    onBackground = DarkBlueOnBackground,
    onSurface = DarkBlueOnSurface
)


private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    tertiary = BlueTertiary,

    background = BlueBackground,
    surface = BlueSurface,

    onPrimary = BlueOnPrimary,
    onSecondary = BlueOnSecondary,
    onTertiary = BlueOnTertiary,
    onBackground = BlueOnBackground,
    onSurface = BlueOnSurface
)


@Composable
fun MyStudyPlannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}