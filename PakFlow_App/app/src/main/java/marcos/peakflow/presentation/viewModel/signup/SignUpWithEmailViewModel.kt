package marcos.peakflow.presentation.viewModel.signup

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Date

class SignUpWithEmailViewModel : ViewModel () {

    private val _userName = MutableLiveData<String>()
    val userName :LiveData<String> = _userName

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _password1 = MutableLiveData<String>()
    val password1 : LiveData<String> = _password1

    private val _password2 = MutableLiveData<String>()
    val password2 : LiveData<String> = _password2

    private val _birthDate = MutableLiveData<Date?>(null)
    val birthDate : LiveData<Date?> = _birthDate

    fun updateBirthDate(date: Date?) {
        _birthDate.value = date
    }

    private val _registerEnable = MutableLiveData<Boolean>()
    val registerEnable: LiveData<Boolean> = _registerEnable

    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> = _isRegistered

    private val _gender = MutableLiveData<String>()
    val gender : LiveData<String> = _gender


    private fun isValidEmail(email: String): Boolean  = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun isValidPassword(password1: String, password2: String): Boolean  = password1.equals(password2)


    fun onRegisterChanged (userName:String, email:String, password1:String, password2:String, birthDate:Date?, gender: String){
        _userName.value = userName
        _email.value = email
        _password1.value = password1
        _password2.value = password2
        _birthDate.value = birthDate
        _gender.value = gender
        _registerEnable.value = isValidEmail(email) && isValidPassword(password1, password2)
    }

    fun onRegisterSelected() {
        TODO("Not yet implemented")
    }
}