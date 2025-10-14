package marcos.peakflow.domain.util

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

//conversion de milisegundos a formato hh:mm:ss
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

// Formateo  tipo de dato Instant a fecha
fun formatInstant(instant: Instant): String {
    val dateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "${dateTime.dayOfMonth} ${dateTime.month.name.lowercase()} ${dateTime.year} " +
            "${"%02d".format(dateTime.hour)}:${"%02d".format(dateTime.minute)}"
}
