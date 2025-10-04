package marcos.peakflow.domain.model.route

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Route(
    val id: String? = null, // Es null antes de guardarse
    @SerialName("user_id") val userId: String?,
    val name: String?,
    @SerialName("start_time") val startTime: Instant,
    @SerialName("end_time") val endTime: Instant?,
    @SerialName("distance_meters") val distance: Double,
    @SerialName("duration_seconds") val durationSec: Long,
    @SerialName("moving_seconds") val movingSec: Long,
    @SerialName("avg_speed_m_s") val avgSpeed: Double,
    @SerialName("max_speed_m_s") val maxSpeed: Double?,
    @SerialName("elevation_gain_m") val elevationGain: Double?,
    @SerialName("elevation_loss_m") val elevationLoss: Double?,
    @SerialName("avg_heart_rate_bpm") val avgHeartRate: Double?,
    @Transient val points: List<RoutePoint>? = null
)
