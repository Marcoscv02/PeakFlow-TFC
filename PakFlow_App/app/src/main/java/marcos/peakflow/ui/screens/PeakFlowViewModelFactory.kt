package marcos.peakflow.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import marcos.peakflow.data.SupabaseReposImpl.RepositoryContainer
import marcos.peakflow.ui.screens.afterLogin.play.PlayViewModel
import marcos.peakflow.ui.screens.afterLogin.userPanel.UserPanelViewModel
import marcos.peakflow.ui.screens.beforeLogin.logueo.loginWithEmail.LoginWithEmailViewModel
import marcos.peakflow.ui.screens.beforeLogin.registro.registerWithEmail.SignUpWithEmailViewModel

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

            modelClass.isAssignableFrom(UserPanelViewModel::class.java) -> {
                UserPanelViewModel(RepositoryContainer.authRepository) as T
            }

            modelClass.isAssignableFrom(PlayViewModel::class.java)-> {
                PlayViewModel() as T
            }

            //Aquí se asignarán mas viewModels a sus respectivos repositorios
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}