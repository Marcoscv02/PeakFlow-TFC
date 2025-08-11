package marcos.peakflow.data.supabasereposImpl

import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.model.route.RoutePoint
import marcos.peakflow.domain.repository.RouteRepository

class RouteSupabaseRepositoryImpl: RouteRepository {
    override suspend fun saveRoute(route: Route): Result<Route> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRoute(routeId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getRoutesByuser(userId: String): Result<List<Route>> {
        TODO("Not yet implemented")
    }

    override suspend fun getRouteById(routeId: String): Result<Route> {
        TODO("Not yet implemented")
    }

    override suspend fun addPoints(routeId: String, points: List<RoutePoint>): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getPoints(routeId: String): Result<List<RoutePoint>> {
        TODO("Not yet implemented")
    }
}