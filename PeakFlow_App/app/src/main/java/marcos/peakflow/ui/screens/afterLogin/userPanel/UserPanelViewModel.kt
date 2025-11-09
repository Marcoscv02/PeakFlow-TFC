package marcos.peakflow.ui.screens.afterLogin.userPanel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import marcos.peakflow.domain.repository.AuthRepository

class UserPanelViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _userName = MutableStateFlow<String>("")
    val userName: StateFlow<String> = _userName

    init {
        viewModelScope.launch {
            // Se obtiene el usuario actual que devuelve un objeto Result<User>
            authRepository.getCurrentUser().onSuccess { user ->
                _userName.value = user.username ?: "Sin Nombre"
                Log.d("UserPanelViewModel", "Nombre de usuario: ${user.username}")
            }.onFailure { exception ->
                _userName.value = "Sin Nombre"
                Log.e("UserPanelViewModel", "Error al obtener el usuario", exception)
            }
        }
    }

    fun closeSession(navigateToInitial: () -> Unit) {
        viewModelScope.launch {
            val success = authRepository.closeSession()
            if (success) {
                navigateToInitial()
            }
        }
    }
}
