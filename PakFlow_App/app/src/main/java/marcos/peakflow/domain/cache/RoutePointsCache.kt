package marcos.peakflow.domain.cache

import marcos.peakflow.domain.model.route.RoutePoint

class RoutePointsCache {
    private val points = mutableListOf<RoutePoint>()

    fun add(point: RoutePoint) {
        points.add(point)
    }

    fun getAll(): List<RoutePoint> = points.toList()

    fun clear() {
        points.clear()
    }
}
