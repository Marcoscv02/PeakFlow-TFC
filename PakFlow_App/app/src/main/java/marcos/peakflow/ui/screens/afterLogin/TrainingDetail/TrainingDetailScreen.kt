package marcos.peakflow.ui.screens.afterLogin.TrainingDetail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import marcos.peakflow.R
import marcos.peakflow.ui.components.BottomNavBar
import marcos.peakflow.ui.components.Screen
import marcos.peakflow.ui.components.StandardTopAppBar
import marcos.peakflow.ui.theme.Black

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainingDetailScreen(
    navigateToHome: () -> Unit,
    navigateToPlay: () -> Unit,
    navigateToRoute: () -> Unit,
    navigateToTraining: () -> Unit,
    navigateBack: () -> Unit,
    ){
    Scaffold (
        topBar = {
            StandardTopAppBar(
                title = "Detalles del entrenamiento",
                onLeftClick = navigateBack,
                leftIcon = R.drawable.back,
                onRightClick = {},
                rightIcon = R.drawable.edit
            )
        } ,

        bottomBar = {
            BottomNavBar(
                currentScreen = Screen.Training.route,
                navigateToHome = navigateToHome,
                navigateToPlay = navigateToPlay,
                navigateToRoute = navigateToRoute,
                navigateToTraining = {navigateToTraining}
            )
        }
    ){
        // Aquí va el contenido principal
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
        ){
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "NOT YET IMPLEMENTED",
                textAlign = TextAlign.Center,
                color = Color.LightGray
            )
            Spacer(modifier = Modifier.weight(1f))

        }
    }
}