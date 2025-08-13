package marcos.peakflow.ui.screens.afterLogin.play

import android.app.Activity
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import marcos.peakflow.R
import marcos.peakflow.data.supabasereposImpl.RouteSupabaseRepositoryImpl
import org.maplibre.android.location.permissions.PermissionsListener
import org.maplibre.android.location.permissions.PermissionsManager

class PlayViewModel(
    private val routeRepository: RouteSupabaseRepositoryImpl
): ViewModel() {

    //Esta variable  almacena si el permiso de ubicación está concedido
    private val _hasLocationPerm = MutableStateFlow(false)
    val hasLocationPerm: StateFlow<Boolean> = _hasLocationPerm

    //instancia de MapLibre para gestionar la petición y callbacks de permiso.
    private var permissionsManager: PermissionsManager? = null



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
}