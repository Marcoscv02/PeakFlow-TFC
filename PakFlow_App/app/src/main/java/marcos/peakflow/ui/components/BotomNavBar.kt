package marcos.peakflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import marcos.peakflow.R
import marcos.peakflow.ui.theme.BackgoundNavSelectedButton
import marcos.peakflow.ui.theme.Gray
import marcos.peakflow.ui.theme.RedPeakFlow

// Definimos una clase sellada para representar cada ruta
sealed class Screen(
    val route: String,
    val title: Int,
    val icon: Int
) {
    object Home : Screen("home", R.string.home, R.drawable.home)
    object Play : Screen("play", R.string.play, R.drawable.play)
    object Route : Screen("route", R.string.route, R.drawable.route)
    object Training : Screen("training", R.string.training, R.drawable.list)
}

@Composable
fun BottomNavBar(
    currentScreen: String?,
    navigateToHome: () -> Unit,
    navigateToPlay: () -> Unit,
    navigateToRoute: () -> Unit,
    navigateToTraining: () -> Unit
) {
    val items = listOf(
        Screen.Home,
        Screen.Play,
        Screen.Route,
        Screen.Training
    )

    NavigationBar (
        modifier = Modifier
            .height(70.dp)
            .background(color = Gray)
            .padding(horizontal = 5.dp)
        ,
        containerColor = Gray,
        tonalElevation = 0.dp
    ){
        items.forEach { screen ->
            NavigationBarItem(
                modifier = Modifier
                    .size(25.dp)
                ,
                selected = currentScreen == screen.route,
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = stringResource(screen.title),
                    )
                },
                label = { Text(
                    stringResource(screen.title),
                    textAlign = TextAlign.Center,
                    fontSize = 11.sp
                ) },

                onClick = {
                    when (screen) {
                        Screen.Home -> navigateToHome()
                        Screen.Play -> navigateToPlay()
                        Screen.Route -> navigateToRoute()
                        Screen.Training -> navigateToTraining()
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = BackgoundNavSelectedButton ,     // Fondo del ítem seleccionado
                    selectedIconColor = RedPeakFlow,     // Color del ícono seleccionado
                    unselectedIconColor = Color.LightGray,
                    selectedTextColor = RedPeakFlow,
                    unselectedTextColor = Color.LightGray
                )
            )
        }

    }
}

