package marcos.peakflow.domain.usecase.route

import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.repository.RouteRepository

class GetUserRoutesUseCase(
    private val routeRepository: RouteRepository
) {
    suspend operator fun invoke(): Result<List<Route>> {
        return routeRepository.getUserRoutes()
    }
}