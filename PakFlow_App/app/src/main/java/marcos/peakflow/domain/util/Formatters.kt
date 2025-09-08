package marcos.peakflow.domain.util

fun formatTime(ms: Long): String {
    val totalSec = ms / 1000
    val h = totalSec / 3600
    val m = (totalSec % 3600) / 60
    val s = totalSec % 60
    return String.format("%02d:%02d:%02d", h, m, s)
}

// Conversión de velocidad de m/s a km/min
fun msToKmPerMin(speedMs: Double): Double {
    return speedMs * 0.06
}