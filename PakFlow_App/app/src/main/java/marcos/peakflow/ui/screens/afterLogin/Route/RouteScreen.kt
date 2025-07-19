package marcos.peakflow.ui.screens.afterLogin.Route

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import marcos.peakflow.R
import marcos.peakflow.ui.components.BottomNavBar
import marcos.peakflow.ui.components.Screen
import marcos.peakflow.ui.components.StandardTopAppBar
import marcos.peakflow.ui.theme.Black

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RouteScreen(
    navigateToHome: () -> Unit,
    navigateToPlay: () -> Unit,
    navigateToTraining: () -> Unit,
    navigateBack: () -> Unit

){
    Scaffold(
        topBar = { StandardTopAppBar(
            title = stringResource(R.string.routeTitleScreen),
            onLeftClick = { navigateBack ()},
            leftIcon = R.drawable.back,
            onRightClick = {  },
            rightIcon = R.drawable.upload
        ) },
        bottomBar = {
            BottomNavBar(
                currentScreen = Screen.Route.route,
                navigateToHome = navigateToHome,
                navigateToPlay = navigateToPlay,
                navigateToRoute = {},
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