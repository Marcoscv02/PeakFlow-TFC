package marcos.peakflow.ui.screens.afterLogin.userPanel

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
            // Supabase mantiene el usuario actual en auth
            val currentUser = authRepository.getCurrentUser()
            _userName.value = currentUser?.username ?: "Sin Nombre"
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
