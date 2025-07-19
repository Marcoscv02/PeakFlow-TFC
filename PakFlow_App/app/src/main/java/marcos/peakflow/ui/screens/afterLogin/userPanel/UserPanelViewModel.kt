package marcos.peakflow.ui.screens.afterLogin.userPanel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import marcos.peakflow.data.SupabaseReposImpl.AuthSupabaseRepositoryImpl

class UserPanelViewModel(
    private val authRepository: AuthSupabaseRepositoryImpl
): ViewModel() {
    fun closeSession(navigateToInitial: () -> Unit) {
        viewModelScope.launch {
            val success = authRepository.closeSession()
            if (success) {
                navigateToInitial()
            }
        }
    }
}