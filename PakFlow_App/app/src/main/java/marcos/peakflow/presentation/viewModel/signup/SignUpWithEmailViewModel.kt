package marcos.peakflow.presentation.viewModel.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionSource
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import marcos.peakflow.SupabaseClient.supabase
import marcos.peakflow.data.UserProfile

class SignUpWithEmailViewModel : ViewModel (

) {

    private val _userName = MutableLiveData<String>()
    val userName :LiveData<String> = _userName

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password1 = MutableLiveData<String>()
    val password1 : LiveData<String> = _password1

    private val _password2 = MutableLiveData<String>()
    val password2 : LiveData<String> = _password2

    private val _birthDate = MutableLiveData<LocalDate?>(null)
    val birthDate : LiveData<LocalDate?> = _birthDate

    private val _registerEnable = MutableLiveData<Boolean>(false)
    val registerEnable: LiveData<Boolean> = _registerEnable

    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> = _isRegistered

    private val _gender = MutableLiveData<String>()
    val gender : LiveData<String> = _gender

    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage


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

    fun registerUserP





    rofile(
        username: String,
        email: String,
        password: String,
        birthDate: LocalDate?,
        gender: String,
        onSuccess: (UserProfile) -> Unit,
        onError: (String) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. Registro del usuario
                val user = supabase.auth.signUpWith(Email) {
                    this.email = email
                    this.password = password
                }


                // 2. Crear objeto UserProfile
                val userProfile = UserProfile(
                    username = username,
                    birthdate = birthDate,
                    gender = gender
                )

                // 3. Insertar en la tabla 'user_profiles'
                val response = supabase.from("user")
                    .insert(userProfile)


//                if (response.error != null) {
//                    onError("Error al guardar perfil: ${response.error?.message}")
//                } else {
//                    onSuccess(userProfile)
//                }

                supabase.auth.sessionStatus.collect {
                    when(it) {
                        is SessionStatus.Authenticated -> {
                            println("Received new authenticated session.")
                            when(it.source) { //Check the source of the session
                                SessionSource.External -> TODO()
                                is SessionSource.Refresh -> TODO()
                                is SessionSource.SignIn -> TODO()
                                is SessionSource.SignUp -> TODO()
                                SessionSource.Storage -> TODO()
                                SessionSource.Unknown -> TODO()
                                is SessionSource.UserChanged -> TODO()
                                is SessionSource.UserIdentitiesChanged -> TODO()
                                SessionSource.AnonymousSignIn -> TODO()
                            }
                        }
                        SessionStatus.Initializing -> println("Initializing")
                        is SessionStatus.RefreshFailure -> println("Refresh failure ${it.cause}") //Either a network error or a internal server error
                        is SessionStatus.NotAuthenticated -> {
                            if(it.isSignOut) {
                                println("User signed out")
                            } else {
                                println("User not signed in")
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                onError("Error inesperado: ${e.localizedMessage}")
            }
        }
    }
}