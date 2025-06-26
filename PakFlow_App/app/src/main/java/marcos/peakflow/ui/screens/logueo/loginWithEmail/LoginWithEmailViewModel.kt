package marcos.peakflow.ui.screens.logueo.loginWithEmail

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import marcos.peakflow.data.SupabaseReposImpl.AuthSupabaseRepositoryImpl

class LoginWithEmailViewModel(
    private val userSupabaseRepositoryImpl: AuthSupabaseRepositoryImpl
) : ViewModel (){

    private val _userNameOrEmail = MutableLiveData<String>()
    val  userNameOrEmail : MutableLiveData<String> = _userNameOrEmail

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    private fun IsValidEmail(email: String): Boolean  = Patterns.EMAIL_ADDRESS.matcher(email).matches()


    fun OnLoginChanged(userNameOrEmail: String, password: String) {
        _userNameOrEmail.value = userNameOrEmail
        _password.value = password
    }

    fun OnLoginSelected() {
        TODO("Not yet implemented")
    }
}