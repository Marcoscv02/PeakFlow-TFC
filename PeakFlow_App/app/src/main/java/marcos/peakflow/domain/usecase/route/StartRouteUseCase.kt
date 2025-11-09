package marcos.peakflow.domain.usecase.route

import kotlinx.datetime.Clock
import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.service.GpsService


class StartRouteUseCase(
    private val gpsService: GpsService
) {
    operator fun invoke(userId: String): Route {
        val route = Route(
            userId = userId,
            name = null,
            startTime = Clock.System.now(),
            endTime = null,
            distance = 0.0,
            durationSec = 0,
            movingSec = 0,
            avgSpeed = 0.0,
            maxSpeed = null,
            elevationGain = null,
            elevationLoss = null,
            avgHeartRate = null,
            points = null
        )

        gpsService.start()
        return route

    }
}