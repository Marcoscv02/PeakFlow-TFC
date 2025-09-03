package marcos.peakflow.ui.screens.afterLogin.play

import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import marcos.peakflow.R
import marcos.peakflow.domain.repository.RouteRepository
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.engine.LocationEngine
import org.maplibre.android.location.engine.LocationEngineCallback
import org.maplibre.android.location.engine.LocationEngineRequest
import org.maplibre.android.location.engine.LocationEngineResult
import org.maplibre.android.location.permissions.PermissionsListener
import org.maplibre.android.location.permissions.PermissionsManager

class PlayViewModel(
    routeRepository: RouteRepository
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
    val isRecording: StateFlow<Boolean> = _isRunning

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



    /**
     * Este metodo lo llama el Activity en el onCreate
     *
     *  - Comprueba si hai permiso `areLocationPermissionsGranted`
     *  - Si lo hay, actualiza la variable `_hasLocationPerm`
     *  - Si no, crea y lanza la petición de permisos
     *
     *  @param Activity
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
     * El ViewModel **no** crea contextos UI ni MapView; solo suscribe/remueve callbacks.
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
            Log.e("PlayViewModel", "Error al solicitar la ubicacion actual del usuario", e)
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
     * Si por alguna razón recibes Location desde otra parte (broadcast, sensor...), puedes actualizar manualmente
     */
    fun updateLocation(location: Location) {
        _userLocation.value = LatLng(location.latitude, location.longitude)
    }

    fun toggleRunning() {
        if (_isRunning.value) {
            stopTimer()
        } else {
            _isRecording.value = true // se marca en el primer arranque
            startTimer()
        }
    }



    //FUNCIONES CRONÓMETRO
    /**
     * Inicializa cronómetro
     */
    private fun startTimer() {
        _isRunning.value = true
        job = viewModelScope.launch {
            val start = System.currentTimeMillis() - _elapsedTime.value
            while (_isRunning.value) {
                _elapsedTime.value = System.currentTimeMillis() - start
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
