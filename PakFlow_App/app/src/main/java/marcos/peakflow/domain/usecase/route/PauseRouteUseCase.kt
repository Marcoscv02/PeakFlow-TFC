package marcos.peakflow.domain.usecase.route

import marcos.peakflow.data.services.AndroidGpsService

class PauseRouteUseCase(
    private val gpsService: AndroidGpsService
) {
    operator fun invoke() {
        gpsService.pause()
    }
}
