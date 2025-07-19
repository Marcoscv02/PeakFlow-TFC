package marcos.peakflow.domain.model.route

import java.util.Date

data class GpxTrack(
    val name: String,
    val startTime: Date = Date(),
    var endTime: Date? = null,
    val points: MutableList<TrackPoint> = mutableListOf()
) {
    // Datos calculados
    var totalDistance: Double = 0.0   // En metros
    var avgSpeed: Double = 0.0        // En m/s
}
