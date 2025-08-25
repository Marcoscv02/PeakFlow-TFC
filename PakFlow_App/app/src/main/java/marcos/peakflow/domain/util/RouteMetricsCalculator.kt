package marcos.peakflow.domain.util

import kotlinx.datetime.Instant
import marcos.peakflow.domain.model.route.RoutePoint
import kotlin.math.*

// Distancia total usando Haversine
fun calculateDistance(points: List<RoutePoint>): Double {
    if (points.size < 2) return 0.0
    return points.zipWithNext { a, b ->
        haversine(a.latitude, a.longitude, b.latitude, b.longitude)
    }.sum()
}

private fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val R = 6371000.0 // radio tierra en m
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2).pow(2) +
            cos(Math.toRadians(lat1)) *
            cos(Math.toRadians(lat2)) *
            sin(dLon / 2).pow(2)
    return 2 * R * atan2(sqrt(a), sqrt(1 - a))
}

// Duración total
fun calculateDurationSec(startTimeIso: Instant, endTimeIso: Instant): Long {
    val start = startTimeIso
    val end = endTimeIso
    return (end - start).inWholeSeconds
}

// Tiempo en movimiento (solo segmentos con velocidad > umbral)
fun calculateMovingTime(points: List<RoutePoint>, minSpeed: Double = 0.5): Long {
    if (points.size < 2) return 0
    return points.zipWithNext { a, b ->
        if ((a.speed ?: 0.0) > minSpeed) {
            (b.timestamp - a.timestamp).inWholeSeconds
        } else 0
    }.sum()
}

// Ganancia de elevación
fun calculateElevationGain(points: List<RoutePoint>): Double {
    return points.zipWithNext { a, b ->
        val diff = (b.altitude ?: 0.0) - (a.altitude ?: 0.0)
        if (diff > 0) diff else 0.0
    }.sum()
}

// Pérdida de elevación
fun calculateElevationLoss(points: List<RoutePoint>): Double {
    return points.zipWithNext { a, b ->
        val diff = (b.altitude ?: 0.0) - (a.altitude ?: 0.0)
        if (diff < 0) -diff else 0.0
    }.sum()
}

// Frecuencia cardíaca promedio
fun calculateAvgHeartRate(points: List<RoutePoint>): Double {
    val hrs = points.mapNotNull { it.heartRate }
    return if (hrs.isNotEmpty()) hrs.average() else 0.0
}
