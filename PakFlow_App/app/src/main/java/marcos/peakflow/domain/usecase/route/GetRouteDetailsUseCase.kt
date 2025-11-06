package marcos.peakflow.domain.usecase.route

import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.model.route.RoutePoint
import marcos.peakflow.domain.repository.RouteRepository

class GetRouteDetailsUseCase(
    private val routeRepository: RouteRepository
) {
    suspend operator fun invoke(routeId: String): Result<Route> { // 1. Cambiamos el tipo de retorno a lo que queremos: un solo Result<Route>
        val routeResult = routeRepository.getRouteById(routeId)
        val pointsResult = routeRepository.getPoints(routeId)

        return if (routeResult.isSuccess && pointsResult.isSuccess) {
            val routeObject = routeResult.getOrThrow()    // devuelve objeto Route
            val pointsList = pointsResult.getOrThrow()      // devuelve List<RoutePoint>

            Result.success(routeObject.copy(points = pointsList))
        } else {
            val error = routeResult.exceptionOrNull() ?: pointsResult.exceptionOrNull()
            Result.failure(error ?: Exception("Error al cargar los detalles de la ruta"))
        }
    }
}
