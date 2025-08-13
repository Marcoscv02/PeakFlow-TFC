package marcos.peakflow.domain.usecase.route

import marcos.peakflow.domain.model.route.RoutePoint
import marcos.peakflow.domain.repository.RouteRepository

class AddRoutePointUseCase(
    private val routeRepository: RouteRepository
) {
    suspend operator fun invoke(routeId: String, point: RoutePoint): Result<Unit> {
        return routeRepository.addPoints(routeId, listOf(point))
    }
}
