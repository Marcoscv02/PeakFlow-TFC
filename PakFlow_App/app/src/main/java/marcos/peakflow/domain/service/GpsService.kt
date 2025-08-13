package marcos.peakflow.domain.service

import kotlinx.coroutines.flow.Flow
import marcos.peakflow.domain.model.route.RoutePoint

interface GpsService {
    fun start()
    fun pause()
    fun resume()
    fun stop()

    val locationUpdates: Flow<RoutePoint>
}
