package on.the.stove.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColors(
    primary = Color(0xffffa726),
    primaryVariant = Color(0xffffd95b),
    secondary = Color(0xff90a4ae),
    secondaryVariant = Color(0xffc1d5e0),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
)
private val LightColors = lightColors(
    primary = Color(0xffffa726),
    primaryVariant = Color(0xffc77800),
    secondary = Color(0xff90a4ae),
    secondaryVariant = Color(0xff62757f),
    onPrimary = Color(0xFF000000),
    onSecondary = Color(0xFF000000),
)

@Composable
internal fun OnTheStoveTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = if (isSystemInDarkTheme()) DarkColors else LightColors,
        content = content,
    )
}