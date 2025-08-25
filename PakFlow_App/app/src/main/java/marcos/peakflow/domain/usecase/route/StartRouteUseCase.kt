package marcos.peakflow.domain.usecase.route

import kotlinx.datetime.Clock
import marcos.peakflow.data.services.AndroidGpsService
import marcos.peakflow.domain.model.route.Route


class StartRouteUseCase(
    private val gpsService: AndroidGpsService
) {
    operator fun invoke(userId: String, name: String?): Route {
        val route = Route(
            userId = userId,
            name = name,
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