package marcos.peakflow.domain.util

fun formatTime(ms: Long): String {
    val totalSec = ms / 1000
    val h = totalSec / 3600
    val m = (totalSec % 3600) / 60
    val s = totalSec % 60
    return String.format("%02d:%02d:%02d", h, m, s)
}

// Conversión de velocidad de m/s a min/km
fun msToMinPerKm(speedMps: Double): Double {
    if (speedMps <= 0) return 0.0
    val pace = 1000.0 / (speedMps * 60.0)
    return kotlin.math.round(pace * 100) / 100
}