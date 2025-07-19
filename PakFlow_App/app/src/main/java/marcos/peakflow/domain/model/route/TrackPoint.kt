package marcos.peakflow.domain.model.route

import java.util.Date

data class TrackPoint(
    // Datos base
    val lat: Double,
    val lon: Double,
    val time: Date,

    // Datos adicionales
    val ele: Double? = null,          // Altitud
    val speed: Float? = null,         // Velocidad en m/s
    val accuracy: Float? = null       // Precisión horizontal
)
