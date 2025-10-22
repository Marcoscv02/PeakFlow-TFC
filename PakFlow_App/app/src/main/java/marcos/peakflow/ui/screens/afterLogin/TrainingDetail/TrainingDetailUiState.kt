package marcos.peakflow.ui.screens.afterLogin.TrainingDetail

import marcos.peakflow.domain.model.route.Route

class TrainingDetailUiState (
    val route: Route,
    val isLoading: Boolean = true,
    val error: String? = null
)