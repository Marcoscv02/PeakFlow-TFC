package marcos.peakflow.ui.screens.beforeLogin.registro.registerWithEmail

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import marcos.peakflow.data.supabasereposimpl.AuthSupabaseRepositoryImpl
import marcos.peakflow.domain.model.user.User
import marcos.peakflow.domain.repository.AuthRepository
import marcos.peakflow.ui.screens.beforeLogin.registro.UserState

class SignUpWithEmailViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    //Estado unificado
    private val _userState = MutableStateFlow(UserState())
    val userState :StateFlow<UserState?> = _userState.asStateFlow()

    // Cache para evitar validaciones redundantes
    private var lastValidation: String? = null

    /**
     * Pequeñas funciones lambda para validar email y contraseña
     */
    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun isValidPassword(p1: String, p2: String): Boolean =
        p1 == p2 && p1.length >= 8


    /**
     * Metodo que se llama desde la screen composable para actualizar los distintos campos
     * @param String:userName
     * @param String:email
     * @param String:password1
     * @param String:password2
     * @param LocalDate:birthDate
     * @param String:gender
     */
    fun updateFields(
        userName: String = _userState.value.username,
        email: String = _userState.value.email,
        password1: String = _userState.value.password1,
        password2: String = _userState.value.password2,
        birthDate: LocalDate? = _userState.value.birthdate,
        gender: String = _userState.value.gender
    ) {
        _userState.update { currentState ->
            // Actualizar campos directamente en el estado
            val updatedState = currentState.copy(
                username = userName,
                email = email,
                password1 = password1,
                password2 = password2,
                birthdate = birthDate,
                gender = gender
            )

            // Verificar si los campos relevantes cambiaron
            val fieldsChanged = currentState.username != userName ||
                    currentState.email != email ||
                    currentState.password1 != password1 ||
                    currentState.password2 != password2 ||
                    currentState.birthdate != birthDate ||
                    currentState.gender != gender

            val newValidation = if (fieldsChanged) {
                validateFields(
                    userName,
                    email,
                    password1,
                    password2,
                    birthDate,
                    gender
                )
            } else lastValidation

            lastValidation = newValidation

            updatedState.copy(
                isRegisterEnabled = newValidation == null,
                errorMessage = if (fieldsChanged) newValidation else currentState.errorMessage
            )
        }
    }



    /**
     * Método  que se encarga de validar que todos los campos son correctos y mandar un mensaje de error en caso contrario
     * @param String:userName
     * @param String:email
     * @param String:password1
     * @param String:password2
     * @param LocalDate:birthDate
     * @param String:gender
     *
     * @return String- Error message
     */
    private fun validateFields(
        userName: String,
        email: String,
        password1: String,
        password2: String,
        birthDate: LocalDate?,
        gender: String
    ): String? = when {
        userName.isBlank() -> "El nombre de usuario es obligatorio"
        !isValidEmail(email) -> "El correo electrónico no es válido"
        !isValidPassword(password1, password2) ->
            "Las contraseñas deben coincidir y tener al menos 8 caracteres"
        birthDate == null -> "Debes seleccionar una fecha de nacimiento"
        gender.isBlank() -> "Debes seleccionar un género"
        else -> null
    }


    /**
     * Método del viewModel para registrar un nuevo usuario (Llama al método de validar los campos, y si no se prouce ningun error llama al metodo del repositorio para registrar el nuevo usuario)
     * @throws Exception
     */
    fun registerUser() {
        val state = _userState.value
        val error = lastValidation ?: validateFields(
            state.username,
            state.email,
            state.password1,
            state.password2,
            state.birthdate,
            state.gender
        )

        if (error != null) {
            _userState.update { it.copy(errorMessage = error) }
            return
        }

        if (state.isLoading) return

        viewModelScope.launch {
            _userState.update { it.copy(isLoading = true, errorMessage = null) }

            try {
                val user = User(
                    username = state.username,
                    email = state.email,
                    birthdate = state.birthdate,
                    gender = state.gender
                )

                authRepository.registerUser(user, state.password1)
                _userState.update { it.copy(isRegistered = true, isLoading = false) }

            } catch (e: Exception) {
                Log.e("UserRegister", "Error en registro: ${e.message}")
                _userState.update {
                    it.copy(
                        errorMessage = e.message ?: "Error en el registro",
                        isLoading = false
                    )
                }
            }
        }
    }
}