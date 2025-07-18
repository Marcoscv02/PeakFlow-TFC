package marcos.peakflow.ui.screens.afterLogin.trainings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
fun TrainingsScreen(
    navigateToHome: () -> Unit,
    navigateToPlay: () -> Unit,
    navigateToRoute: () -> Unit,
    navigateBack: () -> Unit
){
    Scaffold(
        topBar = {
            TripleTopAppBar(
                title = stringResource(R.string.trainingsTitleScreen),
                onLeftClick = navigateBack,
                leftIcon = R.drawable.back,
                onMidleClick = {},
                middleIcon = R.drawable.search,
                onRightClick = {},
                rightIcon = R.drawable.filter
            )
        },
        bottomBar = {
            BottomNavBar(
                currentScreen = Screen.Training.route,
                navigateToHome = navigateToHome,
                navigateToPlay = navigateToPlay,
                navigateToRoute = navigateToRoute,
                navigateToTraining = {}
            )
        }
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
        ){
            //Aquí va el contenido
        }
    }
}

