package marcos.peakflow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    primary = RedPeakFlow,
    secondary = Gray,
    tertiary = Black,
    onPrimary = Color.White,       // Cor do texto/iconas sobre a cor primaria
    background = Black ,
    onBackground = Color.White,    // Cor do texto/iconas sobre o fondo
    onSurface = Color.White
)

private val LightColorScheme = lightColorScheme(

    primary = RedPeakFlow,
    background = WhiteBackground,  // Fondo principal claro
    surface = WhiteSurface,        // Superficies claras (Cards brancas)
    onPrimary = Color.White,       // Texto sobre botóns primarios (vermellos)
    onBackground = Color.Black,    // Texto principal sobre o fondo claro
    onSurface = Color.Black        // Texto sobre as superficies claras
)

@Composable
fun ThemeChangerTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colors,
        typography = Typography(),
        content = content
    )
}