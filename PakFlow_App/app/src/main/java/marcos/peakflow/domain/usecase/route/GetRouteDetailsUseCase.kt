package marcos.peakflow.domain.usecase.route

import marcos.peakflow.data.supabasereposimpl.RouteSupabaseRepositoryImpl
import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.model.route.RoutePoint

class GetRouteDetailsUseCase(
    private val routeRepository: RouteSupabaseRepositoryImpl
) {
    suspend operator fun invoke(routeId: String): Result<Pair<Route, List<RoutePoint>>> {
        val route = routeRepository.getRouteById(routeId)
        val points = routeRepository.getPoints(routeId)
        return if (route.isSuccess && points.isSuccess) {
            Result.success(route.getOrThrow() to points.getOrThrow())
        } else {
            Result.failure(Exception("Error loading route details"))
        }
    }
}
