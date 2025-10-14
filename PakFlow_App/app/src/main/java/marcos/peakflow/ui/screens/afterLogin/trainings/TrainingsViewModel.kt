package marcos.peakflow.ui.screens.afterLogin.trainings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import marcos.peakflow.data.RepositoryContainer.routeRepository
import marcos.peakflow.domain.usecase.route.GetUserRoutesUseCase

class TrainingsViewModel (
    getUserRoutesUseCase: GetUserRoutesUseCase,

): ViewModel(){
    //Variable que capta el estado de la UI en esta pantalla
    private val _uiState = MutableStateFlow(TrainingsUIState())
    val uiState: StateFlow<TrainingsUIState> = _uiState

    init {
        loadUserRoutes()
    }

    private fun loadUserRoutes() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = routeRepository.getUserRoutes()
            result.onSuccess { routes ->
                _uiState.value = TrainingsUIState(routes = routes, isLoading = false)
            }.onFailure { exception ->
                _uiState.value = TrainingsUIState(error = exception.message, isLoading = false)
            }
        }
    }
}