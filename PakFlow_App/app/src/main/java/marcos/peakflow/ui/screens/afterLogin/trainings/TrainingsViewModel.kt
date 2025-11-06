package marcos.peakflow.ui.screens.afterLogin.trainings

import android.util.Log
import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import marcos.peakflow.data.RepositoryContainer.routeRepository
import marcos.peakflow.domain.usecase.route.DeleteRouteUseCase
import marcos.peakflow.domain.usecase.route.GetUserRoutesUseCase

class TrainingsViewModel (
    private val getUserRoutesUseCase: GetUserRoutesUseCase,
    private val deleteRouteUseCase: DeleteRouteUseCase

): ViewModel(){
    //Variable que capta el estado de la UI en esta pantalla
    private val _uiState = MutableStateFlow(TrainingsUIState())
    val uiState: StateFlow<TrainingsUIState> = _uiState


    /**
     * Declara que al cargarse la pantalla se debe ejecutar el metodo loadUserRoutes
     */
    init {
        loadUserRoutes()
    }

    /**
     * Metodo que carga las rutas del usuario actual
     * @return Unit
     */
    private fun loadUserRoutes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = getUserRoutesUseCase()
            result.onSuccess { routes ->
                _uiState.value = TrainingsUIState(routes = routes, isLoading = false)
            }.onFailure { exception ->
                _uiState.value = TrainingsUIState(error = exception.message, isLoading = false)
            }
        }
    }

    /**
     * Metodo del ViewModel que elimina una ruta
     * @param String routeId
     * @return Unit
     */
    fun deleteRoute(routeId: String){
        viewModelScope.launch {
            val result = deleteRouteUseCase(routeId)
            result.onSuccess {
                // Se o borrado foi exitoso, actualiza a UI localmente.
                _uiState.update { currentState ->
                    val updatedRoutes = currentState.routes.filterNot { it.id == routeId }
                    currentState.copy(routes = updatedRoutes)
                }
            }.onFailure { exception ->
                // Se falla, actualiza a UI para mostrar un erro.
                _uiState.update {
                    it.copy(error = "Error al eliminar la ruta: ${exception.message}")
                }
            }
        }
    }
}