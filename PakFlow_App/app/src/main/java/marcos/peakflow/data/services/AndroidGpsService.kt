package marcos.peakflow.data.services

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.datetime.Clock
import marcos.peakflow.domain.model.route.RoutePoint
import marcos.peakflow.domain.service.GpsService

class AndroidGpsService(
    private val context: Context
) : GpsService {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    private val _updates = MutableSharedFlow<RoutePoint>(extraBufferCapacity = 64)
    override val locationUpdates: Flow<RoutePoint> = _updates.asSharedFlow()

    private var isRunning = false




    @SuppressLint("MissingPermission")
    override fun start() {
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000L // cada segundo
        ).build()

        fusedClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
        isRunning = true
    }

    override fun pause() {
        isRunning = false
    }

    override fun resume() {
        isRunning = true
    }

    override fun stop() {
        fusedClient.removeLocationUpdates(locationCallback)
        isRunning = false
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            if (!isRunning) return
            val loc = result.lastLocation ?: return
            val point = RoutePoint(
                routeId = "", // lo asigna supabase
                timestamp = Clock.System.now(),
                latitude = loc.latitude,
                longitude = loc.longitude,
                altitude = loc.altitude,
                speed = loc.speed.toDouble(),
                heartRate = null // integrar si se obtiene de otro servicio
            )
            _updates.tryEmit(point)
        }
    }
}
