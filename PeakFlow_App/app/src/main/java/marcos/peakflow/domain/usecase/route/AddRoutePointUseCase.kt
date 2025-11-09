package marcos.peakflow.domain.usecase.route

import marcos.peakflow.domain.cache.RoutePointsCache
import marcos.peakflow.domain.model.route.RoutePoint

class AddRoutePointUseCase(
    private val cache: RoutePointsCache,
) {
    operator fun invoke(point: RoutePoint) {
        cache.add(point)
    }
}
