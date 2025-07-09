package marcos.peakflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import marcos.peakflow.ui.screens.afterLogin.Route.RouteScreen
import marcos.peakflow.ui.screens.afterLogin.home.HomeScreen
import marcos.peakflow.ui.screens.afterLogin.play.PlayScreen
import marcos.peakflow.ui.screens.afterLogin.trainings.TrainingsScreen
import marcos.peakflow.ui.screens.beforeLogin.initial.InitialScreen
import marcos.peakflow.ui.screens.beforeLogin.logueo.login.LoginScreen
import marcos.peakflow.ui.screens.beforeLogin.logueo.loginWithEmail.LoginWithEmailScreen
import marcos.peakflow.ui.screens.beforeLogin.registro.registerWithEmail.RegisterWithEmailScreen
import marcos.peakflow.ui.screens.beforeLogin.registro.signup.SignUpScreen


@Composable
fun NavigationWrapper() {

        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Home){


            composable<Initial> {
                InitialScreen(
                    navigateToLogin = {navController.navigate(Login)},
                    navigateToSignup = {navController.navigate(Signup)}
                )
            }

            composable<Login> {
                LoginScreen(
                    navigateToSignup = {
                         navController.navigate(Signup){
                            popUpTo<Initial> { inclusive= false } //Eliminar toda la pila anterior de vistas creadas cuando se navega a uno de estos

                        }
                    },
                    navigateToLoginWithEmail = {navController.navigate(LoginWithEmail)}
                )
            }

            composable<Signup> {
                SignUpScreen(
                    navigateToLogin = {
                        navController.navigate(Login){
                            popUpTo<Initial> { inclusive = false }
                        }
                    },
                    navigateToRegisterWithEmail = {navController.navigate(RegisterWithEmail)}
                )
            }

            composable<LoginWithEmail> {
                LoginWithEmailScreen(
                    navigateToLogin = {navController.popBackStack()},
                    navigateToHome =  {navController.navigate(Home)}
                )
            }

            composable<RegisterWithEmail> {
                RegisterWithEmailScreen(
                    navigateToSignup = {navController.popBackStack()},
                    navigateToHome =  {navController.navigate(Home)}
                )
            }

            composable<Home> {
                HomeScreen(
                    navigateToPlay = {navController.navigate(Play)},
                    navigateToRoute = {navController.navigate(Route)},
                    navigateToTraining = {navController.navigate(Training)},
                    navigateToUserPanel = {},
                    navigateToNotifPanel = {}
                )
            }

            composable<Play>{
                PlayScreen(
                    navigateToHome = {navController.navigate(Home)},
                    navigateToRoute = {navController.navigate(Route)},
                    navigateToTraining = {navController.navigate(Training)},
                    navigateBack = {navController.popBackStack()}
                )
            }

            composable<Route>{
                RouteScreen(
                    navigateToHome = {navController.navigate(Home)},
                    navigateToPlay = {navController.navigate(Play)},
                    navigateToTraining = {navController.navigate(Training)},
                    navigateBack = {navController.popBackStack()}

                )
            }

            composable<Training> {
                TrainingsScreen(
                    navigateToHome = {navController.navigate(Home)},
                    navigateToPlay = {navController.navigate(Play)},
                    navigateToRoute = {navController.navigate(Route)}
                )
            }
        }
}