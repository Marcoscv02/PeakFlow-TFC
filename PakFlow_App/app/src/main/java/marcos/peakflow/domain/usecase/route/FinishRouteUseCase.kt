package marcos.peakflow.domain.usecase.route

import kotlinx.datetime.Clock
import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.model.route.RoutePoint
import marcos.peakflow.domain.repository.RouteRepository
import marcos.peakflow.domain.service.GpsService

class FinishRouteUseCase(
    private val routeRepository: RouteRepository,
    private val gpsService: GpsService
) {

//    suspend operator fun invoke(
//        routeId: String,
//        startTime: String,
//        points: List<RoutePoint>
//    ): Result<Route> {
//        gpsService.stop()
//
//       // Calcular métricas finales
//        val distance = calculateDistance(points)
//        val durationSec = calculateDurationSec(startTime, nowIso())
//        val movingSec = calculateMovingTime(points)
//        val avgSpeed = if (durationSec > 0) distance / durationSec else 0.0
//        val maxSpeed = points.maxOfOrNull { it.speed ?: 0.0 }
//        val elevationGain = calculateElevationGain(points)
//        val elevationLoss = calculateElevationLoss(points)
//        val avgHeartRate = calculateAvgHeartRate(points)
//
//        // Obtener ruta actual
//        val route = routeRepository.getRouteById(routeId).getOrThrow()
//
//
//    val updatedRoute = route.copy(
//        endTime = Clock.System.now(),
//      distance = distance,
//      durationSec = durationSec,
//      movingSec = movingSec,
//      avgSpeed = avgSpeed,
//      maxSpeed = maxSpeed,
//      elevationGain = elevationGain,
//      elevationLoss = elevationLoss,
//      avgHeartRate = avgHeartRate
//      )
//        return routeRepository.saveRoute(updatedRoute)
//    }
}
