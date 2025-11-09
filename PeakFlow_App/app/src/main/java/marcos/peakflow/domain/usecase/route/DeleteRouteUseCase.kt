package marcos.peakflow.domain.usecase.route

import marcos.peakflow.domain.repository.RouteRepository

class DeleteRouteUseCase(
    private val routeRepository: RouteRepository
) {
    /**
     * Invoca el caso de uso para eliminar una ruta por su ID.
     * @param routeId el ID de la ruta a eliminar.
     * @return Un `Result` que indica si la operación fue exitosa.
     */
    suspend operator fun invoke(routeId: String): Result<Boolean> {

        return routeRepository.deleteRoute(routeId)
    }
}
