package marcos.peakflow.domain.usecase.route

import kotlinx.datetime.Clock
import marcos.peakflow.domain.cache.RoutePointsCache
import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.repository.RouteRepository
import marcos.peakflow.domain.service.GpsService
import marcos.peakflow.domain.util.calculateAvgHeartRate
import marcos.peakflow.domain.util.calculateDistance
import marcos.peakflow.domain.util.calculateDurationSec
import marcos.peakflow.domain.util.calculateElevationGain
import marcos.peakflow.domain.util.calculateElevationLoss
import marcos.peakflow.domain.util.calculateMovingTime

class FinishRouteUseCase(
    private val routeRepository: RouteRepository,
    private val cache: RoutePointsCache,
    private val gpsService: GpsService
) {

    suspend operator fun invoke(
        route: Route,
        name: String
    ): Boolean {
        gpsService.stop()

        // Tomar puntos acumulados en la cache y mandar error si no hay
        val points = cache.getAll()
        if (points.isEmpty()){
            System.err.println("No points to save in cache memory")
            return false
        }





        // Calcular métricas finales
        val distance = calculateDistance(points)
        val durationSec = calculateDurationSec(route.startTime, Clock.System.now())
        val movingSec = calculateMovingTime(points)
        val avgSpeed = if (durationSec > 0) distance / durationSec else 0.0
        val maxSpeed = points.maxOfOrNull { it.speed ?: 0.0 }
        val elevationGain = calculateElevationGain(points)
        val elevationLoss = calculateElevationLoss(points)
        val avgHeartRate = calculateAvgHeartRate(points)



        val updatedRoute = route.copy(
            name = name,
           endTime = Clock.System.now(),
           distance = distance,
           durationSec = durationSec,
           movingSec = movingSec,
           avgSpeed = avgSpeed,
           maxSpeed = maxSpeed,
           elevationGain = elevationGain,
           elevationLoss = elevationLoss,
           avgHeartRate = 0.0,
            points = points
        )
        return routeRepository.saveRoute(updatedRoute).isSuccess
    }
}
