package marcos.peakflow.domain.usecase.route

import marcos.peakflow.domain.model.route.RoutePoint
import marcos.peakflow.domain.repository.RouteRepository
import marcos.peakflow.domain.service.GpsService

class AddRoutePointUseCase(
    private val routeRepository: RouteRepository,
    private val gpsService: GpsService
) {
    suspend operator fun invoke(routeId: String, point: RoutePoint): Result<Unit> {
        return routeRepository.addPoints(routeId, listOf(point))
    }
}
