package marcos.peakflow.domain.usecase.route

import marcos.peakflow.data.supabasereposimpl.RouteSupabaseRepositoryImpl
import marcos.peakflow.domain.model.route.Route

class GetUserRoutesUseCase(
    private val routeRepository: RouteSupabaseRepositoryImpl
) {
    suspend operator fun invoke(userId: String): Result<List<Route>> {
        return routeRepository.getRoutesByuser(userId)
    }
}