package marcos.peakflow.domain.repository

import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.model.route.RoutePoint

interface RouteRepository {
    suspend fun saveRoute(route: Route): Result<Boolean>
    suspend fun deleteRoute(routeId: String): Result<Boolean>
    suspend fun getUserRoutes(): Result<List<Route>>
    suspend fun getRouteById(routeId: String): Result<Route>
    suspend fun addPoints(routeId: String, points: List<RoutePoint>): Result<Unit>
    suspend fun getPoints(routeId: String): Result<List<RoutePoint>>
    suspend fun getLastRouteSaved(): Result<Route>
}