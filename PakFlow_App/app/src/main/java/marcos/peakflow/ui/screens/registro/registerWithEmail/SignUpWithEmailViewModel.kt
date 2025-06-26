package marcos.peakflow.ui.screens.registro.registerWithEmail

import android.util.Patterns
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.LocalDate
import marcos.peakflow.data.SupabaseReposImpl.AuthSupabaseRepositoryImpl
import marcos.peakflow.domain.model.User

class SignUpWithEmailViewModel( private val authRepository: AuthSupabaseRepositoryImpl ): ViewModel () {

    private val _userState = MutableStateFlow<User?>(null)
    val userState :StateFlow<User?> = _userState

    private val _userName = MutableStateFlow<String>("")
    val userName :StateFlow<String> = _userName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email : StateFlow<String> = _email

    private val _password1 = MutableStateFlow("")
    val password1 : StateFlow<String> = _password1

    private val _password2 = MutableStateFlow("")
    val password2 : StateFlow<String> = _password2

    private val _birthDate = MutableStateFlow<LocalDate?>(null)
    val birthDate : StateFlow<LocalDate?> = _birthDate.asStateFlow()

    private val _gender = MutableStateFlow("")
    val gender : StateFlow<String> = _gender

    private val _registerEnable = MutableStateFlow(false)
    val registerEnable: StateFlow<Boolean> = _registerEnable

    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered


    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    private fun isValidEmail(email: String): Boolean  = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun isValidPassword(p1: String, p2: String): Boolean =
        p1 == p2 && p1.length >= 8


    fun validateFieldsAndRegister() {
        val username = _userName.value.orEmpty()
        val email = _email.value.orEmpty()
        val password1 = _password1.value.orEmpty()
        val password2 = _password2.value.orEmpty()
        val birthdate = _birthDate.value
        val gender = _gender.value.orEmpty()


        val error = when {
            username.isBlank() -> "El nombre de usuario es obligatorio"
            !isValidEmail(email) -> "El correo electrónico no es válido"
            !isValidPassword(password1, password2) -> "Las contraseñas deben coincidir y tener al menos 8 caracteres"
            birthdate == null -> "Debes seleccionar una fecha de nacimiento"
            gender.isBlank() -> "Debes seleccionar un género"
            else -> null
        }

        _errorMessage.value = error

        if (error == null) {

        }
    }


    fun onRegisterChanged (userName:String, email:String, password1:String, password2:String, birthDate:LocalDate?, gender: String){
        _userName.value = userName
        _email.value = email
        _password1.value = password1
        _password2.value = password2
        _birthDate.value = birthDate
        _gender.value = gender
    }

}