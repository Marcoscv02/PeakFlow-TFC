package marcos.peakflow.ui.screens.afterLogin.trainings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import marcos.peakflow.R
import marcos.peakflow.ui.components.BottomNavBar
import marcos.peakflow.ui.components.Screen
import marcos.peakflow.ui.theme.Black
import marcos.peakflow.ui.theme.Gray

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainingsScreen(
    navigateToHome: () -> Unit,
    navigateToPlay: () -> Unit,
    navigateToRoute: () -> Unit,
){
    Scaffold(
        topBar = {
            TrainingTopAppBar(
                onLeftClick = {},
                onMidleClick = {},
                onRightClick = {}
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


@Composable
fun TrainingTopAppBar(
    onLeftClick: () -> Unit,
    onMidleClick: () -> Unit,
    onRightClick: () -> Unit,
) {
    Surface(
        color = Gray,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icono izquierdo (alineado al inicio)
            IconButton(
                onClick = onLeftClick,
                modifier = Modifier
                    .weight(0.9f, fill = false)
                    .padding(horizontal = 5.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "leftIcon",
                    tint = Color.LightGray,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Texto central (con peso máximo para centrar)
            Text(
                text = stringResource(R.string.trainingsTitleScreen),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.LightGray,
                modifier = Modifier
                    .weight(3.5f) // Peso mayor para centrar en el espacio disponible
                    .wrapContentWidth(Alignment.CenterHorizontally),
                textAlign = TextAlign.End
            )

            // Contenedor para iconos derechos (alineados al final)
            Row(
                modifier = Modifier
                    .weight(1f, fill = true)
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onMidleClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "MiddleIcon",
                        tint = Color.LightGray,
                        modifier = Modifier.size(21.dp)
                    )
                }
                IconButton(onClick = onRightClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter),
                        contentDescription = "RightIcon",
                        tint = Color.LightGray,
                        modifier = Modifier.size(21.dp)
                    )
                }
            }
        }
    }
}