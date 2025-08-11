package marcos.peakflow.domain.repository

import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.model.route.RoutePoint

interface RouteRepository {
    suspend fun saveRoute(route: Route): Result<Route>
    suspend fun deleteRoute(routeId: String): Boolean
    suspend fun getRoutesByuser(userId: String): Result<List<Route>>
    suspend fun getRouteById(routeId: String): Result<Route>
    suspend fun addPoints(routeId: String, points: List<RoutePoint>): Result<Unit>
    suspend fun getPoints(routeId: String): Result<List<RoutePoint>>
}