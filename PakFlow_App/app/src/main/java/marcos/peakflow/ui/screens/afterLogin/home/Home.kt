package marcos.peakflow.ui.screens.afterLogin.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import marcos.peakflow.R
import marcos.peakflow.ui.components.BottomNavBar
import marcos.peakflow.ui.components.Screen
import marcos.peakflow.ui.components.StandardTopAppBar
import marcos.peakflow.ui.theme.Black


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToPlay: () -> Unit,
    navigateToRoute: () -> Unit,
    navigateToTraining: () -> Unit,
    navigateToUserPanel: () -> Unit,
    navigateToNotifPanel: () -> Unit
) {
    Scaffold(
        topBar = { StandardTopAppBar(
            title = stringResource(R.string.homeTitleScreen),
            onLeftClick = navigateToUserPanel,
            leftIcon = R.drawable.user,
            onRightClick = {  },
            rightIcon = R.drawable.notification
        ) },
        bottomBar = {
            BottomNavBar(
                currentScreen = Screen.Home.route,
                navigateToHome = {},
                navigateToPlay = navigateToPlay,
                navigateToRoute = navigateToRoute,
                navigateToTraining = navigateToTraining
            )
        },
        containerColor = Black
    ) {
        // Aquí va el contenido principal
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
        ){

        }

    }
}


