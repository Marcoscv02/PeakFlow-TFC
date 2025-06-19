package marcos.peakflow.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import marcos.peakflow.presentation.view.initial.InitialScreen
import marcos.peakflow.presentation.view.login.LoginScreen
import marcos.peakflow.presentation.view.login.LoginWithEmailScreen
import marcos.peakflow.presentation.view.signup.RegisterWithEmailScreen
import marcos.peakflow.presentation.view.signup.SignUpScreen
import marcos.peakflow.presentation.viewModel.login.LoginWithEmailViewModel
import marcos.peakflow.presentation.viewModel.signup.SignUpWithEmailViewModel


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
                    LoginWithEmailViewModel(),
                    navigateToLogin = {navController.popBackStack()},
                    navigateToHome =  {navController.navigate(Home)}
                )
            }

            composable<RegisterWithEmail> {
                RegisterWithEmailScreen(
                    SignUpWithEmailViewModel(),
                    navigateToSignup = {navController.popBackStack()},
                    navigateToHome =  {navController.navigate(Home)}
                )
            }



        }
}