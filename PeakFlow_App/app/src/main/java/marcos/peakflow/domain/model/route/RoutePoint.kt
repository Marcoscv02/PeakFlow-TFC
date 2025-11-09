package marcos.peakflow.domain.model.route

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoutePoint(
    val id: String? = null, // Es null antes de guardarse
    @SerialName("route_id") val routeId: String?,
    @SerialName ("created_at") val timestamp: Instant,
    val latitude: Double,
    val longitude: Double,
    @SerialName("altitude_m") val altitude: Double?,
    @SerialName("speed_m_s") val speed: Double?,
    @SerialName("heart_rate_bpm") val heartRate: Int?,
)
