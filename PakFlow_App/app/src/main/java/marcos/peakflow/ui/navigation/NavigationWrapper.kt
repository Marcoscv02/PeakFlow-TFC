package marcos.peakflow.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import marcos.peakflow.ui.screens.home.HomeScreen
import marcos.peakflow.ui.screens.initial.InitialScreen
import marcos.peakflow.ui.screens.logueo.login.LoginScreen
import marcos.peakflow.ui.screens.logueo.loginWithEmail.LoginWithEmailScreen
import marcos.peakflow.ui.screens.registro.registerWithEmail.RegisterWithEmailScreen
import marcos.peakflow.ui.screens.registro.signup.SignUpScreen
import marcos.peakflow.ui.screens.logueo.loginWithEmail.LoginWithEmailViewModel
import marcos.peakflow.ui.screens.registro.registerWithEmail.SignUpWithEmailViewModel


@Composable
fun NavigationWrapper() {

        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = Initial){


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
                HomeScreen()
            }
        }
}