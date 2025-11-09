package marcos.peakflow.ui.screens.beforeLogin.registro

import kotlinx.datetime.LocalDate
import java.util.UUID

 data class UserState(
    val uuid: UUID? = null,
    val username: String = "",
    val email: String = "",
    val password1: String = "",
    val password2: String = "",
    val birthdate: LocalDate? = null,
    val gender: String = "",
    val isRegisterEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isRegistered: Boolean = false,
    val errorMessage: String? = null
)
