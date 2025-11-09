package marcos.peakflow.domain.usecase.route

import marcos.peakflow.domain.service.GpsService

class ResumeRouteUseCase(
    private val gpsService: GpsService
) {
    operator fun invoke() {
        gpsService.resume()
    }
}
