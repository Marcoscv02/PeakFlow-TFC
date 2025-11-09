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

/**
 * Servicio GPS para Android basado en FusedLocationProvider.
 *
 * Responsabilidad:
 * - Pide actualizaciones de ubicación al sistema.
 * - Convierte cada ubicación en un [RoutePoint].
 * - Emite los puntos por un [Flow] frío-caliente (SharedFlow).
 *
 * Ciclo de vida:
 * - [start] comienza a recibir ubicaciones del sistema.
 * - [pause] deja de emitir sin dejar de escuchar (ahorra lógica de re-suscripción).
 * - [resume] vuelve a emitir.
 * - [stop] corta la suscripción al sistema.
 *
 * Requisitos:
 * - Permisos en tiempo de ejecución: ACCESS_FINE_LOCATION o equivalentes.
 * - Servicios de Google Play disponibles.
 *
 * Notas de diseño:
 * - Se usa [MutableSharedFlow] con buffer para no bloquear el callback.
 * - Si el buffer se llena, `tryEmit` puede descartar puntos (mejor que reventar la UI).
 */
class AndroidGpsService(
    private val context: Context
) : GpsService {

    //Cliente de ubicaciones fusionadas de Google.
    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)
    //Flujo interno y mutable de puntos de ruta con 64 eventos de margen para no bloquear el hilo del callback.
    private val _updates = MutableSharedFlow<RoutePoint>(extraBufferCapacity = 64)
    //Flujo público y de solo lectura con las ubicaciones emitidas.
    override val locationUpdates: Flow<RoutePoint> = _updates.asSharedFlow()
    private var isRunning = false

    /**
     * Empieza a solicitar ubicaciones al sistema.
     *
     * Detalles:
     * - Intervalo deseado: 1000 ms (1 segundo).
     * - Prioridad: [Priority.PRIORITY_HIGH_ACCURACY].
     * - Callback se ejecuta en el hilo principal.
     */
    @SuppressLint("MissingPermission")
    override fun start() {
        // Construye una solicitud de ubicación "rápida y precisa".
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1000L // cada segundo
        ).build()

        // Se registra el callback para recibir actualizaciones.
        fusedClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
        isRunning = true
    }

    /**
     * Pausa la emisión al flujo sin desregistrar el callback del sistema.
     */
    override fun pause() {
        isRunning = false
    }

    /**
     * Reanuda la emisión al flujo.
     */
    override fun resume() {
        isRunning = true
    }

    /**
     * Detiene por completo la escucha de ubicaciones.
     * Libera el callback y ahorra batería.
     */
    override fun stop() {
        fusedClient.removeLocationUpdates(locationCallback)
        isRunning = false
    }

    /**
     * Callback que recibe ubicaciones del sistema y las transforma en [RoutePoint].
     *
     * Flujo de datos:
     * - Si [isRunning] es false, se ignora la actualización.
     * - Se toma `lastLocation` del [LocationResult].
     * - Se construye un [RoutePoint] con:
     *   - `timestamp`: ahora (UTC) vía [Clock.System.now].
     *   - `latitude`/`longitude`: del sistema.
     *   - `altitude`: metros sobre el nivel del mar (si lo provee el dispositivo).
     *   - `speed`: en m/s, convertido a Double.
     *   - `routeId`: vacío; lo definirá el backend.
     * - Se emite con `tryEmit` para no suspender el callback.
     *
     * Nota:
     * - Si el buffer del [_updates] está lleno, `tryEmit` puede descartar el punto.
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            if (!isRunning) return // pausa "suave": no emitimos
            val loc = result.lastLocation ?: return
            val point = RoutePoint(
                routeId = "", // lo asigna supabase
                timestamp = Clock.System.now(),
                latitude = loc.latitude,
                longitude = loc.longitude,
                altitude = loc.altitude,
                speed = loc.speed.toDouble(),
                heartRate = 0 // integrar si se obtiene de otro servicio
            )
            _updates.tryEmit(point) // emisión no bloqueante
        }
    }
}
