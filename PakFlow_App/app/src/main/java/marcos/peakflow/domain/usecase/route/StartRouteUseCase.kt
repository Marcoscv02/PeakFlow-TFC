package marcos.peakflow.domain.usecase.route

import kotlinx.datetime.Clock
import marcos.peakflow.data.supabasereposimpl.RouteSupabaseRepositoryImpl
import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.service.GpsService


class StartRouteUseCase(
    private val routeRepository: RouteSupabaseRepositoryImpl,
    private val gpsService: GpsService
) {
    suspend operator fun invoke(userId: String, name: String?): Result<Route> {
        val route = Route(
            userId = userId,
            name = name,
            startTime = Clock.System.now(),
            endTime = null,
            distance = 0.0,
            durationSec = 0,
            movingSec = 0,
            avgSpeed = 0.0,
            maxSpeed = null,
            elevationGain = null,
            elevationLoss = null,
            avgHeartRate = null,
            points = null
        )
        return routeRepository.saveRoute(route).onSuccess {
            gpsService.start()
        }
    }
}