package marcos.peakflow.ui.screens.afterLogin.play

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import marcos.peakflow.R
import marcos.peakflow.domain.cache.RoutePointsCache
import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.model.route.RoutePoint
import marcos.peakflow.domain.repository.AuthRepository
import marcos.peakflow.domain.usecase.route.AddRoutePointUseCase
import marcos.peakflow.domain.usecase.route.FinishRouteUseCase
import marcos.peakflow.domain.usecase.route.PauseRouteUseCase
import marcos.peakflow.domain.usecase.route.ResumeRouteUseCase
import marcos.peakflow.domain.usecase.route.StartRouteUseCase
import marcos.peakflow.domain.util.calculateCurrentSpeed
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.engine.LocationEngine
import org.maplibre.android.location.engine.LocationEngineCallback
import org.maplibre.android.location.engine.LocationEngineRequest
import org.maplibre.android.location.engine.LocationEngineResult
import org.maplibre.android.location.permissions.PermissionsListener
import org.maplibre.android.location.permissions.PermissionsManager
import kotlinx.datetime.Clock

class PlayViewModel(
    private val startRouteUseCase: StartRouteUseCase,
    private val pauseRouteUseCase: PauseRouteUseCase,
    private val resumeRouteUseCase: ResumeRouteUseCase,
    private val finishRouteUseCase: FinishRouteUseCase,
    private val addRoutePointUseCase: AddRoutePointUseCase,
    private val authRepository: AuthRepository,
): ViewModel() {

    //Esta variable  almacena si el permiso de ubicación está concedido
    private val _hasLocationPerm = MutableStateFlow(false)
    val hasLocationPerm: StateFlow<Boolean> = _hasLocationPerm

    // Última ubicación conocida del usuario
    private val _userLocation = MutableStateFlow<LatLng?>(null)
    val userLocation: StateFlow<LatLng?> = _userLocation

    // URL y key del estilo de MapLibre
    val mapTilerKey = "ayEMMbyitB7UOUpklBr6"
    val styleUrl = "https://api.maptiler.com/maps/hybrid/style.json?key=$mapTilerKey"

    //Indica si el entrenamiento esta pausado o activo
    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    //Indica el tiempo transcurrido desde el inicio de la grabación
    private val _elapsedTime = MutableStateFlow(0L) // milisegundos
    val elapsedTime: StateFlow<Long> = _elapsedTime

    //Indica si hay una grabacion en curso
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording

    // Indica si el mapa/engine está listo
    private val _mapReady = MutableStateFlow(false)
    val mapReady: StateFlow<Boolean> = _mapReady

    private var job: Job? = null

    //instancia de MapLibre para gestionar la petición y callbacks de permiso.
    private var permissionsManager: PermissionsManager? = null

    // Engine de ubicación y callback
    private var locationEngine: LocationEngine? = null
    private val locationCallback = object : LocationEngineCallback<LocationEngineResult> {
        override fun onSuccess(result: LocationEngineResult) {
            result.lastLocation?.let {
                _userLocation.value = LatLng(it.latitude, it.longitude)
            }
        }

        override fun onFailure(exception: Exception) {
            // opcional: exponer un error vía StateFlow si quieres
        }
    }

    //Ruta activa
    private lateinit var route: Route



    /**
     * Este metodo lo llama el Activity en el onCreate
     *
     * - Comprueba si hai permiso `areLocationPermissionsGranted`
     * - Si lo hay, actualiza la variable `_hasLocationPerm`
     * - Si no, crea y lanza la petición de permisos
     *
     * @param Activity
     */
    fun checkOrRequestPermissions(activity: Activity) {
        if (PermissionsManager.areLocationPermissionsGranted(activity)) {
            _hasLocationPerm.value = true
        } else {
            permissionsManager = PermissionsManager(object : PermissionsListener {


                override fun onExplanationNeeded(msgs: List<String>) {
                    val message = activity.getString(R.string.EnableLocationRequest)
                    Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
                }
                override fun onPermissionResult(granted: Boolean) {
                    _hasLocationPerm.value = granted
                }
            })
            permissionsManager!!.requestLocationPermissions(activity)
        }
    }



    /**
     * metodo que la Activity debe llamar desde su propio callback para reenviar los datos al `permissionsManager` de MapLibre y que éste dispare internamente `onPermissionResult`.
     * @param int requestCode
     * @param Array perms
     * @param IntArray results
     */
    fun onRequestPermissionsResult(
        requestCode: Int,
        perms: Array<String>,
        results: IntArray
    ) {
        permissionsManager?.onRequestPermissionsResult(requestCode, perms, results)
    }



    /**
     * Inicia las actualizaciones de ubicación usando el LocationEngine que crea la UI.
     * El ViewModel **no** crea contextos UI ni MapView; solo subscribe/remueve callbacks.
     *
     * @param LocationEngine engine
     */
    @SuppressLint("MissingPermission")
    fun startLocationUpdates(engine: LocationEngine) {
        // si ya tenemos engine, no volver a suscribir
        if (locationEngine == engine) return

        locationEngine = engine

        // Primer último valor
        try {
            engine.getLastLocation(locationCallback)
        } catch (e: Exception) {
            Log.e("PlayViewModel", "Error al solicitar la ubicación actual del usuario", e)
        }

        // Solicitar updates periódicos
        val request = LocationEngineRequest.Builder(750L)
            .setFastestInterval(750L)
            .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
            .build()

        try {
            engine.requestLocationUpdates(request, locationCallback, null)
        } catch (e: Exception) {
            Log.e("PlayViewModel", "Error al solicitar actualizaciones de ubicación", e)
        }
    }

    /**
     * Para las actualizaciones y limpia referencias
     */
    fun stopLocationUpdates() {
        try {
            locationEngine?.removeLocationUpdates(locationCallback)
        } catch (e: Exception) {
            Log.e("PlayViewModel", "Error al intentar elimar las actualizaciones de ubicacion", e)
        }
        locationEngine = null
    }



    /**
     * Metodo que se llama cuando se inicia un entrenamiento
     */
    fun onStartRoute() {
        viewModelScope.launch {
            try {
                // Obtiene usuario y ruta en IO
                val currentUser = withContext(Dispatchers.IO) { authRepository.getCurrentUser() }
                val newRoute = withContext(Dispatchers.IO) { startRouteUseCase(currentUser?.id) }

                route = newRoute // guarda ruta en caché

                // Inicia cronómetro
                startTimer()

                _isRecording.value = true
                Log.i("PlayViewModel", "Entrenamiento iniciado")

            } catch (e: Exception) {
                _isRecording.value = false
                Log.e("PlayViewModel", "Error al iniciar ruta", e)
            }
        }
    }

    /**
     * Metodo que se llama cuando se finaliza un entrenamiento
     * @param String:name
     */
    fun onFinishRoute(name: String) {
        viewModelScope.launch {
            val result = finishRouteUseCase(
                route = route,          // tu ruta activa
                name = name
            )

            result.onSuccess {
                Log.i("PlayViewModel", "Ruta finalizada y guardada: $it")
            }.onFailure {
                Log.e("PlayViewModel", "Error al finalizar ruta", it)
            }
        }
    }



    /**
     * Metodo que se llama cuando se pausa/reanuda un entrenamiento
     */
    fun toggleRunning() {
        if (_isRunning.value) {
            stopTimer()
            pauseRouteUseCase()
            _isRunning.value = false
            Log.i("PlayViewModel", " Entrenamiento pausado")
        } else {
            startTimer()
            resumeRouteUseCase()
            _isRunning.value = true
            Log.i("PlayViewModel", " Entrenamiento reanudado")


        }
    }

    /**
     * Metodo que se encarga de poner el estado de la variable _mapReady a true cuando se llama
     */
    fun setMapReady() {
        _mapReady.value = true
    }



    //FUNCIONES CRONÓMETRO
    /**
     * Inicializa cronómetro
     */
    private fun startTimer() {
        _isRunning.value = true
        job = viewModelScope.launch {
            val start = System.currentTimeMillis() - _elapsedTime.value
            val cache = RoutePointsCache()
            val points = cache.getAll()

            while (_isRunning.value) {
                _elapsedTime.value = System.currentTimeMillis() - start

                // Cada segundo guardamos un punto de la ruta
                _userLocation.value?.let { latLng ->
                    val point = RoutePoint(
                        routeId = null,
                        latitude = latLng.latitude,
                        longitude = latLng.longitude,
                        timestamp = Clock.System.now(),
                        speed = calculateCurrentSpeed(points),
                        altitude = latLng.altitude,
                        heartRate = null //Implementacion futura
                    )
                    addRoutePointUseCase(point)
                }


                delay(1000L)
            }
        }
    }
    /**
     * Pausa cronómetro
     */
    private fun stopTimer() {
        _isRunning.value = false
        job?.cancel()
    }
    /**
     * Resetea cronómetro
     */
    fun resetTimer() {
        stopTimer()
        _elapsedTime.value = 0L
    }
}
