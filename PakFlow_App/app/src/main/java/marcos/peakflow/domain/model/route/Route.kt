package marcos.peakflow.domain.model.route

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName

data class Route(
    @SerialName("user_id") val userId: String,
    val name: String?,
    @SerialName("start_time") val startTime: Instant,
    @SerialName("end_time") val endTime: Instant?,
    @SerialName("distance_meters") val distance: Double,
    @SerialName("duration_seconds") val durationSec: Int,
    @SerialName("moving_seconds") val movingSec: Int,
    @SerialName("avg_speed_m_s") val avgSpeed: Double,
    @SerialName("max_speed_m_s") val maxSpeed: Double?,
    @SerialName("elevation_gain_m") val elevationGain: Double?,
    @SerialName("elevation_loss_m") val elevationLoss: Double?,
    @SerialName("avg_heart_rate_bpm") val avgHeartRate: Double?,
    val points: List<RoutePoint>?
)
