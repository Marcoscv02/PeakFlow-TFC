package marcos.peakflow.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import marcos.peakflow.ui.screens.afterLogin.TrainingDetail.TrainingDetailScreen
import marcos.peakflow.ui.screens.afterLogin.home.HomeScreen
import marcos.peakflow.ui.screens.afterLogin.play.PlayScreen
import marcos.peakflow.ui.screens.afterLogin.route.RouteScreen
import marcos.peakflow.ui.screens.afterLogin.settings.SettingsScreen
import marcos.peakflow.ui.screens.afterLogin.trainings.TrainingsScreen
import marcos.peakflow.ui.screens.afterLogin.userPanel.UserPanelScreen
import marcos.peakflow.ui.screens.beforeLogin.SessionViewModel
import marcos.peakflow.ui.screens.beforeLogin.initial.InitialScreen
import marcos.peakflow.ui.screens.beforeLogin.logueo.login.LoginScreen
import marcos.peakflow.ui.screens.beforeLogin.logueo.loginWithEmail.LoginWithEmailScreen
import marcos.peakflow.ui.screens.beforeLogin.registro.registerWithEmail.RegisterWithEmailScreen
import marcos.peakflow.ui.screens.beforeLogin.registro.signup.SignUpScreen


@Composable
fun NavigationWrapper() {

    val navController = rememberNavController()
    val sessionViewModel: SessionViewModel = viewModel()
    val startDestination by sessionViewModel.startDestination.collectAsState()

    // Hasta que no se determine la sesión, se muestra una pantalla de carga
    if (startDestination == null) {
        // Loading screen o SplashScreen
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }else {
        NavHost(navController = navController, startDestination = startDestination!!) {


            composable<Initial> {
                InitialScreen(
                    navigateToLogin = { navController.navigate(Login) },
                    navigateToSignup = { navController.navigate(Signup) }
                )
            }

            composable<Login> {
                LoginScreen(
                    navigateToSignup = {
                        navController.navigate(Signup) {
                            popUpTo<Initial> {
                                inclusive = false
                            } //Eliminar toda la pila anterior de vistas creadas cuando se navega a uno de estos

                        }
                    },
                    navigateToLoginWithEmail = { navController.navigate(LoginWithEmail) }
                )
            }

            composable<Signup> {
                SignUpScreen(
                    navigateToLogin = {
                        navController.navigate(Login) {
                            popUpTo<Initial> { inclusive = false }
                        }
                    },
                    navigateToRegisterWithEmail = { navController.navigate(RegisterWithEmail) }
                )
            }

            composable<LoginWithEmail> {
                LoginWithEmailScreen(
                    navigateToLogin = { navController.popBackStack() },
                    navigateToHome = { navController.navigate(Home) }
                )
            }

            composable<RegisterWithEmail> {
                RegisterWithEmailScreen(
                    navigateToSignup = { navController.popBackStack() },
                    navigateToHome = { navController.navigate(Home) }
                )
            }

            composable<Home> {
                HomeScreen(
                    navigateToPlay = { navController.navigate(Play) },
                    navigateToRoute = { navController.navigate(Route) },
                    navigateToTraining = { navController.navigate(Training) },
                    navigateToUserPanel = { navController.navigate(UserPanel)},
                    navigateToNotifPanel = {}
                )
            }

            composable<UserPanel> {
                UserPanelScreen(
                    navigateToHome = { navController.navigate(Home) },
                    navigateToPlay = { navController.navigate(Play) },
                    navigateToRoute = { navController.navigate(Route) },
                    navigateToTraining = { navController.navigate(Training) },
                    navigateBack = { navController.popBackStack() },
                    navigateToInitial = { navController.navigate(Initial) },
                    navigateToSettings = { navController.navigate(Settings) }
                )
            }

            composable<Settings> {
                SettingsScreen(
                    navigateToHome = { navController.navigate(Home) },
                    navigateToPlay = { navController.navigate(Play) },
                    navigateToRoute = { navController.navigate(Route) },
                    navigateToTraining = { navController.navigate(Training) },
                    navigateBack = { navController.popBackStack() },
                )
            }

            composable<Play> {
                PlayScreen(
                    navigateToHome = { navController.navigate(Home) },
                    navigateToRoute = { navController.navigate(Route) },
                    navigateToTraining = { navController.navigate(Training) },
                    navigateBack = { navController.popBackStack() }
                )
            }

            composable<Route> {
                RouteScreen (
                    navigateToHome = { navController.navigate(Home) },
                    navigateToPlay = { navController.navigate(Play) },
                    navigateToTraining = { navController.navigate(Training) },
                    navigateBack = { navController.popBackStack() }
                )
            }

            composable<Training> {
                TrainingsScreen(
                    navigateToHome = { navController.navigate(Home) },
                    navigateToPlay = { navController.navigate(Play) },
                    navigateToRoute = { navController.navigate(Route) },
                    navigateBack = { navController.popBackStack() },
                    navigateTrainingDetail = {navController.navigate(TrainingDetail)}
                )
            }

            composable<TrainingDetail>{
                TrainingDetailScreen(
                    navigateToHome = { navController.navigate(Home) },
                    navigateToPlay = { navController.navigate(Play) },
                    navigateToRoute = { navController.navigate(Route) },
                    navigateToTraining = { navController.navigate(Training) },
                    navigateBack = { navController.popBackStack() },
                )
            }
        }
    }
}