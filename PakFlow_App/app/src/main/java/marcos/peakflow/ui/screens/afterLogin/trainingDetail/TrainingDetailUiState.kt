package marcos.peakflow.ui.screens.afterLogin.trainingDetail

import marcos.peakflow.domain.model.route.Route

/**
 * Usamos una sealed interface para representar los diferentes estados posibles de la pantalla.
 * Esto asegura que siempre manejemos los casos de Carga, Éxito y Error explícitamente.
 */
sealed interface TrainingDetailUiState {
    /**
     * Estado inicial y mientras se cargan los datos.
     */
    data object Loading : TrainingDetailUiState

    /**
     * Estado cuando la carga falla. Contiene el mensaje de error.
     */
    data class Error(val message: String) : TrainingDetailUiState

    /**
     * Estado de éxito. Contiene el objeto Route, que aquí NUNCA será nulo.
     */
    data class Success(val route: Route) : TrainingDetailUiState
}