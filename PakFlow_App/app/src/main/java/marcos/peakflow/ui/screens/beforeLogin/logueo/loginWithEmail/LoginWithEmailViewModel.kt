package marcos.peakflow.ui.screens.beforeLogin.logueo.loginWithEmail

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import marcos.peakflow.data.SupabaseReposImpl.AuthSupabaseRepositoryImpl
import marcos.peakflow.ui.screens.beforeLogin.logueo.UserState

@Suppress("UNUSED_EXPRESSION", "UnusedDataClassCopyResult")
class LoginWithEmailViewModel(
    private val authRepository: AuthSupabaseRepositoryImpl
) : ViewModel (){

    private val _userState = MutableStateFlow(UserState())
    val userState :StateFlow<UserState?> = _userState.asStateFlow()

    /**
     * Validacion de email con una funcion lambda
     */
    private fun isValidEmail(email: String): Boolean =
        Patterns.EMAIL_ADDRESS.matcher(email).matches()


    /**
     * Metodo que se llama desde la screen composable para actualizar los distintos campos
     * @param String:email
     * @param String:password
     */
    fun updateFields(
        email: String = _userState.value.email,
        password: String = _userState.value.password,
    ) {
        _userState.update { currentState ->
            currentState.copy(
                email = email,
                password = password,
                isLoginEnabled = email.isNotEmpty() && password.isNotEmpty() && isValidEmail(email)

            ).also {
                // Limpiamos errores cuando se modifican los campos
                if (it.errorMessage != null) {
                    it.copy(errorMessage = null)
                } else {
                    it
                }
            }
        }
    }


    /**
     * Método del viewModel para logearse (Llama al método de validar los campos, y si no se prouce ningún error llama al metodo del repositorio para logear un usuario ya existente)
     * @throws Exception: e
     */
    fun loginUser(onResult: (Boolean) -> Unit) {
        val currentState = _userState.value
        val email = currentState.email
        val password = currentState.password

        if (!isValidEmail(email)) {
            _userState.update { it.copy(errorMessage = "El correo electrónico no es válido") }
            onResult(false)
            return
        }

        viewModelScope.launch {
            try {
                _userState.update { it.copy(isLoading = true, errorMessage = null) }

                val success = authRepository.loginUser(email, password)

                // Éxito en login
                _userState.update {
                    it.copy(
                        isLoading = false,
                        // Aquí podrías añadir lógica adicional como isLoggedIn = true
                    )
                }

                onResult(success)

            } catch (e: Exception) {
                Log.e(
                    "LoginWithEmailVM",
                    "Error en login: ${e.message}",
                    e
                )
                _userState.update {
                    it.copy(
                        errorMessage = "Error en inicio de sesión: ${e.message ?: "Error desconocido"}",
                        isLoading = false
                    )
                }
                onResult(false)
            }
        }
    }


    /**
     * Metodo para reestablecer contraseña en caso de olvido
     * @throws Exception: e
     */
    fun resetPassword() {
        val email = _userState.value.email
        if (!isValidEmail(email)) {
            _userState.update { it.copy(errorMessage = "Email inválido para restablecer contraseña") }
            return
        }

        viewModelScope.launch {
            try {
                _userState.update { it.copy(isLoading = true, errorMessage = null) }
                authRepository.resetPassword(email)
                _userState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Instrucciones enviadas a $email"
                    )
                }
            } catch (e: Exception) {
                _userState.update {
                    it.copy(
                        errorMessage = "Error al restablecer contraseña: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }
}