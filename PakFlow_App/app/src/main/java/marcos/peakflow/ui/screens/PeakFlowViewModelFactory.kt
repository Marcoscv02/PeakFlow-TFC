package marcos.peakflow.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import marcos.peakflow.data.RepositoryContainer
import marcos.peakflow.domain.usecase.route.AddRoutePointUseCase
import marcos.peakflow.domain.usecase.route.FinishRouteUseCase
import marcos.peakflow.domain.usecase.route.GetUserRoutesUseCase
import marcos.peakflow.domain.usecase.route.PauseRouteUseCase
import marcos.peakflow.domain.usecase.route.ResumeRouteUseCase
import marcos.peakflow.domain.usecase.route.StartRouteUseCase
import marcos.peakflow.ui.screens.afterLogin.play.PlayViewModel
import marcos.peakflow.ui.screens.afterLogin.trainings.TrainingsViewModel
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

            modelClass.isAssignableFrom(PlayViewModel::class.java) -> {
                val routeRepository = RepositoryContainer.routeRepository
                val gpsService = RepositoryContainer.gpsService
                val cache = RepositoryContainer.routePointsCache

                val startRouteUseCase = StartRouteUseCase(gpsService)
                val pauseRouteUseCase = PauseRouteUseCase (gpsService)
                val resumeRouteUseCase = ResumeRouteUseCase(gpsService)
                val finishRouteUseCase = FinishRouteUseCase(
                    routeRepository = routeRepository,
                    cache = cache,
                    gpsService = gpsService
                )
                val addRoutePointUseCase = AddRoutePointUseCase(cache)
                PlayViewModel(
                    startRouteUseCase = startRouteUseCase,
                    pauseRouteUseCase = pauseRouteUseCase,
                    resumeRouteUseCase = resumeRouteUseCase,
                    finishRouteUseCase = finishRouteUseCase,
                    addRoutePointUseCase = addRoutePointUseCase,
                    authRepository = RepositoryContainer.authRepository
                ) as T
            }

            modelClass.isAssignableFrom(TrainingsViewModel::class.java)->{
                val routeRepository = RepositoryContainer.routeRepository
                TrainingsViewModel(
                    getUserRoutesUseCase = GetUserRoutesUseCase(routeRepository)
                ) as T
            }

            //Aquí se asignarán mas viewModels a sus respectivos repositorios
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}