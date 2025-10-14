package marcos.peakflow.ui.screens.afterLogin.trainings

import marcos.peakflow.domain.model.route.Route

data class TrainingsUIState(
    val routes: List<Route> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)
