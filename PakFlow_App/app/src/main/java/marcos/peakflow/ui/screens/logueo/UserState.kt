package marcos.peakflow.ui.screens.logueo

 data class UserState(
    val email: String ="",
    val password : String = "",
    val isLoginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val alreadyExists: Boolean = false,
    val errorMessage: String? = null
)
