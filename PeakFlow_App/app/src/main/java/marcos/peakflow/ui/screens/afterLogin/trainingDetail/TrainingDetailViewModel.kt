package marcos.peakflow.ui.screens.afterLogin.trainingDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.repository.RouteRepository
import marcos.peakflow.domain.usecase.route.GetRouteDetailsUseCase

class TrainingDetailViewModel (
    private val getRouteDetailsUseCase: GetRouteDetailsUseCase,
    private val routeRepository: RouteRepository
): ViewModel() {
    // El estado inicial de la UI es siempre 'Cargando'.
    private val _uiState = MutableStateFlow<TrainingDetailUiState>(TrainingDetailUiState.Loading)
    val uiState: StateFlow<TrainingDetailUiState> = _uiState.asStateFlow()


    /**
     * Carga os detalles específicos dunha ruta usando o seu ID.
     * Actualiza o `uiState` a Éxito ou Error.
     */
    fun loadRouteDetails(routeId: String) {
        // Si ya estamos cargando o ya tenemos datos, evitamos recargar innecesariamente.
        if (_uiState.value !is TrainingDetailUiState.Loading) {
            _uiState.value = TrainingDetailUiState.Loading
        }

        viewModelScope.launch {
            val result = getRouteDetailsUseCase(routeId)

            result
                .onSuccess { route ->
                    // En caso de éxito, el estado pasa a ser Success y contiene la ruta.
                    _uiState.value = TrainingDetailUiState.Success(route)
                }
                .onFailure { throwable ->
                    // En caso de error, el estado pasa a ser Error.
                    _uiState.value = TrainingDetailUiState.Error(throwable.message ?: "Error desconocido")
                }
        }
    }

    suspend fun editRoute(route: Route, name: String): Boolean{
        var result = routeRepository.editRoute(route, name)
        return result.isSuccess
    }
}