package marcos.peakflow.domain.model.route

import kotlinx.serialization.SerialName

data class Route(
    val id: String,
    @SerialName("user_id") val userId: String,
    val name: String?,
    @SerialName("start_time") val startTime: String,
    @SerialName("end_time") val endTime: String,
    @SerialName("distance_meters") val distance: Double,
    @SerialName("duration_seconds") val durationSec: Int,
    @SerialName("moving_seconds") val movingSec: Int,
    @SerialName("avg_speed_m_s") val avgSpeed: Double,
    @SerialName("max_speed_m_s") val maxSpeed: Double?,
    @SerialName("elevation_gain_m") val elevationGain: Double?,
    @SerialName("elevation_loss_m") val elevationLoss: Double?,
    @SerialName("avg_heart_rate_bpm") val avgHeartRate: Double?
)
