package marcos.peakflow.presentation.viewModel.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpWithEmailViewModel : ViewModel () {

    private val _email = MutableLiveData<String>()
    val email : LiveData<String> = _email
}