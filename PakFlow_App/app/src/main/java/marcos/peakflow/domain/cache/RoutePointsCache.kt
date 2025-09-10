package marcos.peakflow.domain.cache

import android.util.Log
import marcos.peakflow.domain.model.route.RoutePoint

class RoutePointsCache {
    private val points = mutableListOf<RoutePoint>()

    fun add(point: RoutePoint) {
        points.add(point)
        Log.d("RoutePointsCache","Punto grabado: Latitud: ${point.latitude}, Longitud: ${point.longitude}")

    }

    fun getAll(): List<RoutePoint> = points.toList()

    fun clear() {
        points.clear()
    }
}
