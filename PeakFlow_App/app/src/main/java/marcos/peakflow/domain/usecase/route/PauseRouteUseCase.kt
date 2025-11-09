package marcos.peakflow.domain.usecase.route

import marcos.peakflow.domain.service.GpsService

class PauseRouteUseCase(
    private val gpsService: GpsService
) {
    operator fun invoke() {
        gpsService.pause()
    }
}
