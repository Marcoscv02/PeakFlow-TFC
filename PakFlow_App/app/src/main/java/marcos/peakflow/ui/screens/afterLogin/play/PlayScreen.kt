@file:Suppress("UNUSED_EXPRESSION")

package marcos.peakflow.ui.screens.afterLogin.play

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
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
fun PlayScreen(
    navigateToHome: () -> Unit,
    navigateToRoute: () -> Unit,
    navigateToTraining: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = { StandardTopAppBar(
            title = stringResource(R.string.playTitleScreen),
            onLeftClick = { navigateBack ()},
            leftIcon = R.drawable.back,
            onRightClick = {  },
            rightIcon = R.drawable.save
        ) },
        bottomBar = {
            BottomNavBar(
                currentScreen = Screen.Play.route,
                navigateToHome = navigateToHome,
                navigateToPlay = {},
                navigateToRoute = navigateToRoute,
                navigateToTraining = navigateToTraining
            )
        }
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

