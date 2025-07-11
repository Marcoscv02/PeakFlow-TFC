package marcos.peakflow.ui.screens.afterLogin.userPanel

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
import marcos.peakflow.ui.components.TripleTopAppBar
import marcos.peakflow.ui.theme.Black

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserPanelScreen(
    navigateToHome: () -> Unit,
    navigateToPlay: () -> Unit,
    navigateToRoute: () -> Unit,
    navigateToTraining: () -> Unit,
    navigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TripleTopAppBar(
                title = stringResource(R.string.trainingsTitleScreen),
                onLeftClick = navigateBack,
                leftIcon = R.drawable.back,
                onMidleClick = {},
                middleIcon = R.drawable.search,
                onRightClick = {},
                rightIcon = R.drawable.settings
            )
        },
        bottomBar = {
            BottomNavBar(
                currentScreen = Screen.Training.route,
                navigateToHome = navigateToHome,
                navigateToPlay = navigateToPlay,
                navigateToRoute = navigateToRoute,
                navigateToTraining = navigateToTraining
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
        ) {
            //Aquí va el contenido
        }
    }

}