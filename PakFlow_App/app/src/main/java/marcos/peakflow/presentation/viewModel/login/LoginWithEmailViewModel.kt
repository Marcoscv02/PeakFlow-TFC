package marcos.peakflow.presentation.viewModel.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LoginWithEmailViewModel {

    private val _userNameOrEmail = MutableLiveData<String>()
    val  userNameOrEmail : MutableLiveData<String> = _userNameOrEmail

    private val _password = MutableLiveData<String>()
    val password : LiveData<String> = _password

    fun onLoginChanged(userNameOrEmail: String, password: String) {
        _userNameOrEmail.value = userNameOrEmail
        _password.value = password
    }
}