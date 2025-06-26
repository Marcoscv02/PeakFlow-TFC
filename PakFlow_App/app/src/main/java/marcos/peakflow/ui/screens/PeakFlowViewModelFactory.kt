package marcos.peakflow.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import marcos.peakflow.data.SupabaseReposImpl.RepositoryContainer
import marcos.peakflow.ui.screens.logueo.loginWithEmail.LoginWithEmailViewModel
import marcos.peakflow.ui.screens.registro.registerWithEmail.SignUpWithEmailViewModel

class PeakFlowViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginWithEmailViewModel::class.java) -> {
                LoginWithEmailViewModel(RepositoryContainer.authRepository) as T
            }

            modelClass.isAssignableFrom(SignUpWithEmailViewModel::class.java) -> {
                SignUpWithEmailViewModel(RepositoryContainer.authRepository) as T
            }


            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}