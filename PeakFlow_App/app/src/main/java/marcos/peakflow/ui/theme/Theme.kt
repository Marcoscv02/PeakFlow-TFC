package marcos.peakflow.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val DarkColorScheme = darkColorScheme(
    // Colores principales de la marca
    primary = RedPeakFlow,               // Botones principales, elementos activos importantes.
    onPrimary = Color.White,             // Texto e iconos sobre elementos 'primary'.

    // Color secundario
    secondary = Gray,                    // AppBars, fondos de elementos menos importantes.
    onSecondary = Color.LightGray,           // Texto e iconos sobre elementos 'secondary'.

    // Color de fondo general de las pantallas
    background = Black,                  // El color más de fondo de tu app.
    onBackground = WhiteBackground,          // El color del texto principal sobre 'background'.

    // Color de las superficies (tarjetas, diálogos, menús)
    surface = DarkSurface,               // Ligeramente más claro que el fondo para dar profundidad.
    onSurface = Color.White,             // Texto e iconos sobre estas superficies.

    // Color para variantes de superficie (e.g., campos de texto inactivos)
    surfaceVariant = Gray,               // Un color para distinguir superficies secundarias.
    onSurfaceVariant = LightGray,    // Texto sobre 'surfaceVariant'.

    // Color para bordes y divisores
    outline = Gray,                      // Bordes de OutlinedTextField, divisores.

    // Colores de estado
    error = RedError,                    // Para indicar errores en campos de texto, Toasts.
    onError = Color.White,                // Texto e iconos sobre un fondo de error.

    tertiary = PurpleMetric
)

private val LightColorScheme = lightColorScheme(
    // Colores principales de la marca
    primary = RedPeakFlow,
    onPrimary = Color.White,

    // Color secundario
    secondary = LightGray,
    onSecondary = DarkGrayText,

    // Color de fondo general
    background = WhiteBackground,
    onBackground = Color.Black,

    // Color de las superficies
    surface = WhiteSurface,
    onSurface = Color.Black,

    // Color para variantes de superficie
    surfaceVariant = WhiteBackground,
    onSurfaceVariant = DarkGrayText,

    // Color para bordes y divisores
    outline = StrokeGray,

    // Colores de estado
    error = RedError,
    onError = Color.White,

    onTertiary = BlueMetric
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